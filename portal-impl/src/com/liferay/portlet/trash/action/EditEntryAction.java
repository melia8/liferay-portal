/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.trash.action;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.trash.TrashHandlerRegistryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.trash.TrashEntryConstants;
import com.liferay.portlet.trash.model.TrashEntry;
import com.liferay.portlet.trash.service.TrashEntryLocalServiceUtil;
import com.liferay.portlet.trash.service.TrashEntryServiceUtil;
import com.liferay.portlet.trash.util.TrashUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Manuel de la Peña
 * @author Zsolt Berentey
 */
public class EditEntryAction extends PortletAction {

	@Override
	public void processAction(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			List<ObjectValuePair<String, Long>> entryOVPs = null;

			if (cmd.equals(Constants.CHECK)) {
				JSONObject jsonObject = ActionUtil.checkEntry(actionRequest);

				writeJSON(actionRequest, actionResponse, jsonObject);

				return;
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteEntries(actionRequest);
			}
			else if (cmd.equals(Constants.EMPTY_TRASH)) {
				emptyTrash(actionRequest);
			}
			else if (cmd.equals(Constants.MOVE)) {
				entryOVPs = moveEntry(actionRequest);
			}
			else if (cmd.equals(Constants.RENAME)) {
				entryOVPs = restoreRename(actionRequest);
			}
			else if (cmd.equals(Constants.RESTORE)) {
				entryOVPs = restoreEntries(actionRequest);
			}
			else if (cmd.equals(Constants.OVERRIDE)) {
				entryOVPs = restoreOverride(actionRequest);
			}

			if (cmd.equals(Constants.RENAME) || cmd.equals(Constants.RESTORE) ||
				cmd.equals(Constants.OVERRIDE) || cmd.equals(Constants.MOVE)) {

				addRestoreData(
					(LiferayPortletConfig)portletConfig, actionRequest,
					entryOVPs);
			}

			sendRedirect(actionRequest, actionResponse);
		}
		catch (Exception e) {
			SessionErrors.add(actionRequest, e.getClass());
		}
	}

	@Override
	public ActionForward render(
			ActionMapping mapping, ActionForm form, PortletConfig portletConfig,
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws Exception {

		return mapping.findForward(
			getForward(renderRequest, "portlet.trash.view"));
	}

	protected void addRestoreData(
			LiferayPortletConfig liferayPortletConfig,
			ActionRequest actionRequest,
			List<ObjectValuePair<String, Long>> entryOVPs)
		throws Exception {

		if ((entryOVPs == null) || (entryOVPs.size() <= 0)) {
			return;
		}

		List<String> restoreLinks = new ArrayList<String>();
		List<String> restoreMessages = new ArrayList<String>();

		for (int i = 0; i < entryOVPs.size(); i++) {
			ObjectValuePair<String, Long> entryOVP = entryOVPs.get(i);

			TrashHandler trashHandler =
				TrashHandlerRegistryUtil.getTrashHandler(entryOVP.getKey());

			String restoreLink = trashHandler.getRestoreLink(
				actionRequest, entryOVP.getValue());

			String restoreMessage = trashHandler.getRestoreMessage(
				actionRequest, entryOVP.getValue());

			if (Validator.isNull(restoreLink) ||
				Validator.isNull(restoreMessage)) {

				continue;
			}

			restoreLinks.add(restoreLink);
			restoreMessages.add(restoreMessage);
		}

		Map<String, List<String>> data = new HashMap<String, List<String>>();

		data.put("restoreLinks", restoreLinks);
		data.put("restoreMessages", restoreMessages);

		SessionMessages.add(
			actionRequest,
			liferayPortletConfig.getPortletId() +
				SessionMessages.KEY_SUFFIX_DELETE_SUCCESS_DATA, data);

		SessionMessages.add(
			actionRequest,
			liferayPortletConfig.getPortletId() +
				SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
	}

	protected void deleteEntries(ActionRequest actionRequest) throws Exception {
		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		if (trashEntryId > 0) {
			TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
				trashEntryId);

			deleteEntry(entry.getClassName(), entry.getClassPK());

			return;
		}

		long[] deleteEntryIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "deleteThrashEntryIds"), 0L);

		if (deleteEntryIds.length > 0) {
			for (int i = 0; i < deleteEntryIds.length; i++) {
				TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
					deleteEntryIds[i]);

				deleteEntry(entry.getClassName(), entry.getClassPK());
			}

			return;
		}

		String className = ParamUtil.getString(actionRequest, "className");
		long classPK = ParamUtil.getLong(actionRequest, "classPK");

		if (Validator.isNotNull(className) && (classPK > 0)) {
			deleteEntry(className, classPK);
		}
	}

	protected void deleteEntry(String className, long classPK)
		throws Exception {

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			className);

		trashHandler.deleteTrashEntry(classPK);
	}

	protected void emptyTrash(ActionRequest actionRequest) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		TrashEntryServiceUtil.deleteEntries(themeDisplay.getScopeGroupId());
	}

	protected List<ObjectValuePair<String, Long>> getEntryOVPs(
		String className, long classPK) {

		List<ObjectValuePair<String, Long>> entryOVPs =
			new ArrayList<ObjectValuePair<String, Long>>();

		ObjectValuePair<String, Long> entryOVP =
			new ObjectValuePair<String, Long>(className, classPK);

		entryOVPs.add(entryOVP);

		return entryOVPs;
	}

	protected List<ObjectValuePair<String, Long>> moveEntry(
			ActionRequest actionRequest)
		throws Exception {

		long containerModelId = ParamUtil.getLong(
			actionRequest, "containerModelId");
		String className = ParamUtil.getString(actionRequest, "className");
		long classPK = ParamUtil.getLong(actionRequest, "classPK");

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			className);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			className, actionRequest);

		if (trashHandler.isInTrash(classPK)) {
			trashHandler.moveTrashEntry(
				classPK, containerModelId, serviceContext);
		}
		else {
			trashHandler.moveEntry(classPK, containerModelId, serviceContext);
		}

		return getEntryOVPs(className, classPK);
	}

	protected List<ObjectValuePair<String, Long>> restoreEntries(
			ActionRequest actionRequest)
		throws Exception {

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		List<ObjectValuePair<String, Long>> entryOVPs =
			new ArrayList<ObjectValuePair<String, Long>>();

		if (trashEntryId > 0) {
			entryOVPs = restoreEntry(trashEntryId);
		}
		else {
			long[] restoreEntryIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "restoreTrashEntryIds"), 0L);

			for (int i = 0; i < restoreEntryIds.length; i++) {
				entryOVPs.addAll(restoreEntry(restoreEntryIds[i]));
			}
		}

		return entryOVPs;
	}

	protected List<ObjectValuePair<String, Long>> restoreEntry(
			long trashEntryId)
		throws Exception {

		TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
			trashEntryId);

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			entry.getClassName());

		trashHandler.checkDuplicateTrashEntry(
			entry, TrashEntryConstants.DEFAULT_CONTAINER_ID, StringPool.BLANK);

		trashHandler.restoreTrashEntry(entry.getClassPK());

		return getEntryOVPs(entry.getClassName(), entry.getClassPK());
	}

	protected List<ObjectValuePair<String, Long>> restoreOverride(
			ActionRequest actionRequest)
		throws Exception {

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
			trashEntryId);

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			entry.getClassName());

		long duplicateEntryId = ParamUtil.getLong(
			actionRequest, "duplicateEntryId");

		trashHandler.deleteTrashEntries(new long[] {duplicateEntryId});

		trashHandler.restoreTrashEntry(entry.getClassPK());

		return getEntryOVPs(entry.getClassName(), entry.getClassPK());
	}

	protected List<ObjectValuePair<String, Long>> restoreRename(
			ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long trashEntryId = ParamUtil.getLong(actionRequest, "trashEntryId");

		TrashEntry entry = TrashEntryLocalServiceUtil.getTrashEntry(
			trashEntryId);

		TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(
			entry.getClassName());

		String newName = ParamUtil.getString(actionRequest, "newName");

		if (Validator.isNull(newName)) {
			String oldName = ParamUtil.getString(actionRequest, "oldName");

			newName = TrashUtil.getNewName(themeDisplay, oldName);
		}

		trashHandler.updateTitle(entry.getClassPK(), newName);

		trashHandler.restoreTrashEntry(entry.getClassPK());

		return getEntryOVPs(entry.getClassName(), entry.getClassPK());
	}

}