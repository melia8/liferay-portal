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

package com.liferay.portalweb.asset.webcontent.wcwebcontentcomment.ratewcwebcontentcommentap;

import com.liferay.portalweb.asset.assetpublisher.portlet.addportletap.AddPageAPTest;
import com.liferay.portalweb.asset.assetpublisher.portlet.addportletap.AddPortletAPTest;
import com.liferay.portalweb.asset.assetpublisher.portlet.configureportletdisplaystylefullcontent.ConfigurePortletDisplayStyleFullContentTest;
import com.liferay.portalweb.asset.webcontent.wcwebcontent.addnewwcwebcontentapactions.AddNewWCWebContentAPActionsTest;
import com.liferay.portalweb.asset.webcontent.wcwebcontentcomment.bethefirstwcwebcontentcommentap.BeTheFirstWCWebContentCommentAPTest;
import com.liferay.portalweb.asset.webcontent.wcwebcontentcomment.bethefirstwcwebcontentcommentap.ConfigurePortletEnableCommentsTest;
import com.liferay.portalweb.portal.BaseTestSuite;
import com.liferay.portalweb.portal.controlpanel.webcontent.wcwebcontent.addwcwebcontent.TearDownWCWebContentTest;
import com.liferay.portalweb.portal.util.TearDownPageTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class RateWCWebContentCommentAPTests extends BaseTestSuite {
	public static Test suite() {
		TestSuite testSuite = new TestSuite();
		testSuite.addTestSuite(AddPageAPTest.class);
		testSuite.addTestSuite(AddPortletAPTest.class);
		testSuite.addTestSuite(AddNewWCWebContentAPActionsTest.class);
		testSuite.addTestSuite(ConfigurePortletDisplayStyleFullContentTest.class);
		testSuite.addTestSuite(ConfigurePortletEnableCommentsTest.class);
		testSuite.addTestSuite(ConfigurePortletEnableCommentRatingsTest.class);
		testSuite.addTestSuite(BeTheFirstWCWebContentCommentAPTest.class);
		testSuite.addTestSuite(RateWCWebContentCommentAPTest.class);
		testSuite.addTestSuite(TearDownWCWebContentTest.class);
		testSuite.addTestSuite(TearDownPageTest.class);

		return testSuite;
	}
}