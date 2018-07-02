#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
-------------------------------------------------------------------------------
【版本信息】
版本：     v1.0
作者：     crifan

【详细信息】
用于：
【教程】抓取网并网页中所需要的信息 之 Python版 
http://www.crifan.com/crawl_website_html_and_extract_info_using_python/
的示例代码。

-------------------------------------------------------------------------------
"""

#---------------------------------import---------------------------------------
import urllib2;
import re;
#from BeautifulSoup import BeautifulSoup;

#------------------------------------------------------------------------------
def main():
    userMainUrl = "https://asuevents.asu.edu/";
    req = urllib2.Request(userMainUrl);
    resp = urllib2.urlopen(req);
    respHtml = resp.read();
    #print "respHtml=",respHtml; # you should see the ouput html
    
    #Events name format reference
    #<td class="views-field views-field-title">
          #  <a href="/united-states-marine-corps-table-mall-tempe-0">United States Marine Corps - Table on the Mall @ Tempe</a></td>

    eventName = re.findall('<td\s+?class="views-field\sviews-field-title">[\n\r][\s]*<.+?>(?P<h1user>.+?)</a>[\s]*</td>', respHtml);

    #Event date format reference      
    #<span class="date-display-single">Mon Oct 5</span>
    #eventDate = re.findall('<span\sclass="date-display-single">(?P<h1user>.+?)</span>', respHtml);
    eventDate = re.findall('<td\s+?class="views-field views-field-field-startdate-value">[\n\r][\s]*(?P<h1user>.+?)[\s]*</td>', respHtml);
    for index in range(len(eventDate)):
        eventDate[index] = re.sub(r'<.+?><span\sclass="date-display-single">(?P<date>.+?)</span></div>',"\g<date>, ",eventDate[index]);
        eventDate[index] = re.sub(r'<span\sclass="date-display-single">(?P<date>.+?)</span>',"\g<date>",eventDate[index]);

       
    #Event place format reference      
    #<td class="views-field views-field-phpcode">
           # <a href='http://www.asu.edu/map/interactive/?campus=polytechnic&building=UNION'>Student Union, Cooley Ballrooms</a>
    eventPlace = re.findall('<td\s+?class="views-field views-field-phpcode">[\n\r][\s]*(?P<h1user>.+?)[\s]*</td>', respHtml);
    #placePattern = re.compile(r"<.+?>(?P<place>.+?)</a>");
    for index in range(len(eventPlace)):
       eventPlace[index] = re.sub(r"<.+?>(?P<place>.+?)</a>","\g<place>",eventPlace[index]);
    #print "eventPlace= ",eventPlace;
    if(eventName or eventDate or eventPlace):
        name = eventName[1];
        date = eventDate[1];
        place = eventPlace[1];
        
        #place = placePattern.sub("\g<place>",place);
        #place = re.sub(r"<.+?>(?P<place>.+?)</a>","\g<place>",place);
        print "name:",name," date:",date," place@", place;
        print "length of name is ",len(eventName)," length of date is ",len(eventDate)," length of place is ",len(eventPlace);

    #if(name or date or place)
    f = open('event.dat', 'w');
    for s in eventName:
        f.write(s+"\n");
    for s in eventDate:
        f.write(s+"\n");
    for s in eventPlace:
        f.write(s+"\n");
    #f.write(name+" "+date+" "+place);
    f.close;
###############################################################################
if __name__=="__main__":
    main();
