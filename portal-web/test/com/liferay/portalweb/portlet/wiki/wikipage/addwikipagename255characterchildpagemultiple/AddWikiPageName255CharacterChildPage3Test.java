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

package com.liferay.portalweb.portlet.wiki.wikipage.addwikipagename255characterchildpagemultiple;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class AddWikiPageName255CharacterChildPage3Test extends BaseTestCase {
	public void testAddWikiPageName255CharacterChildPage3()
		throws Exception {
		selenium.selectWindow("null");
		selenium.selectFrame("relative=top");
		selenium.open("/web/guest/home/");
		selenium.clickAt("link=Wiki Test Page",
			RuntimeVariables.replace("Wiki Test Page"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=All Pages", RuntimeVariables.replace("All Pages"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace(
				"lllllllll1lllllllll2lllllllll3lllllllll4lllllllll5lllllllll6lllllllll7lllllllll8lllllllll9llllllll10llllllll11llllllll12llllllll13llllllll14llllllll15llllllll16llllllll17llllllll18llllllll19llllllll20llllllll21llllllll22llllllll23llllllll24llllllll25llll"),
			selenium.getText("//tr[4]/td[1]/a"));
		selenium.clickAt("//tr[4]/td[1]/a",
			RuntimeVariables.replace(
				"lllllllll1lllllllll2lllllllll3lllllllll4lllllllll5lllllllll6lllllllll7lllllllll8lllllllll9llllllll10llllllll11llllllll12llllllll13llllllll14llllllll15llllllll16llllllll17llllllll18llllllll19llllllll20llllllll21llllllll22llllllll23llllllll24llllllll25llll"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Add Child Page"),
			selenium.getText("//div[7]/div[1]/span[1]/a/span"));
		selenium.clickAt("//div[7]/div[1]/span[1]/a/span",
			RuntimeVariables.replace("Add Child Page"));
		selenium.waitForPageToLoad("30000");
		selenium.type("//input[@id='_36_title']",
			RuntimeVariables.replace("Wiki Page ChildPage3 Title"));
		selenium.waitForElementPresent(
			"//textarea[@id='_36_editor' and @style='display: none;']");
		assertEquals(RuntimeVariables.replace("Source"),
			selenium.getText("//span[.='Source']"));
		selenium.clickAt("//span[.='Source']",
			RuntimeVariables.replace("Source"));
		selenium.waitForVisible("//a[@class='cke_button_source cke_on']");
		selenium.waitForVisible("//td[@id='cke_contents__36_editor']/textarea");
		selenium.type("//td[@id='cke_contents__36_editor']/textarea",
			RuntimeVariables.replace("Wiki Page ChildPage3 Content"));
		assertEquals(RuntimeVariables.replace("Source"),
			selenium.getText("//span[.='Source']"));
		selenium.clickAt("//span[.='Source']",
			RuntimeVariables.replace("Source"));
		selenium.waitForElementPresent(
			"//textarea[@id='_36_editor' and @style='display: none;']");
		assertTrue(selenium.isVisible(
				"//td[@id='cke_contents__36_editor']/iframe"));
		selenium.selectFrame("//td[@id='cke_contents__36_editor']/iframe");
		selenium.waitForText("//body", "Wiki Page ChildPage3 Content");
		selenium.selectFrame("relative=top");
		selenium.clickAt("//input[@value='Publish']",
			RuntimeVariables.replace("Publish"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace(
				"Your request completed successfully."),
			selenium.getText("//div[@class='portlet-msg-success']"));
		assertEquals(RuntimeVariables.replace("Wiki Page ChildPage1 Title"),
			selenium.getText("xPath=(//div[@class='child-pages']/ul/li/a)[1]"));
		assertEquals(RuntimeVariables.replace("Wiki Page ChildPage2 Title"),
			selenium.getText("xPath=(//div[@class='child-pages']/ul/li/a)[2]"));
		assertEquals(RuntimeVariables.replace("Wiki Page ChildPage3 Title"),
			selenium.getText("xPath=(//div[@class='child-pages']/ul/li/a)[3]"));
		selenium.clickAt("xPath=(//div[@class='child-pages']/ul/li/a)[3]",
			RuntimeVariables.replace("Wiki Page ChildPage3 Title"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Wiki Page ChildPage3 Title"),
			selenium.getText("//h1[@class='header-title']/span"));
		assertEquals(RuntimeVariables.replace("Wiki Page ChildPage3 Content"),
			selenium.getText("//div[@class='wiki-body']/p"));
	}
}