<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:html="http://www.w3.org/TR/REC-html40" xmlns:sitemap="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>XML Sitemap</title>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                <style type="text/css">
                    body {
                    font-family: Verdana, Arial, Helvetica, sans-serif;
                    font-size: 12px;
                    color:black;
                    }

                    #header, #footer {
                    background-color: #B39DDB;
                    border: 1px black solid;
                    border-right: none;
                    border-left: none;
                    padding: 5px 10px;
                    margin: 10px 0px;
                    line-height: 1.7;
                    }

                    #header a, #footer a {
                    color: black;
                    }

                    #footer {
                    margin-top: 15px;
                    }

                    table {
                    width: 100%;
                    }

                    th {
                    text-align: left;
                    border-bottom: 1px solid black;
                    padding-bottom: 10px;
                    padding-left: 5px;
                    }

                    tr.odd {
                    background-color: #D1C4E9;
                    }

                    td {
                    padding: 5px;
                    }
                </style>
            </head>
            <body>
                <h1>XML Sitemap</h1>
                <div id="header">
                    <p>
                        This sitemap was created using software developed by Universal Development <a href="http://universal-development.com/" title="Custom software development">http://universal-development.com/</a>, check with us if you need support at sitemapgenerator
                        [at] universal-development.com
                    </p>
                </div>
                <div id="content">
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <th style="width: 60%;">Location</th>
                            <th>Priority</th>
                            <th>Change Frequency</th>
                            <th>Last Modified (GMT)</th>
                        </tr>
                        <xsl:for-each select="sitemap:urlset/sitemap:url">
                            <tr>
                                <xsl:if test="position() mod 2 != 0">
                                    <xsl:attribute name="class">odd</xsl:attribute>
                                </xsl:if>
                                <td>
                                    <xsl:variable name="itemURL">
                                        <xsl:value-of select="sitemap:loc"/>
                                    </xsl:variable>
                                    <a href="{$itemURL}">
                                        <xsl:value-of select="sitemap:loc"/>
                                    </a>
                                </td>
                                <td>
                                    <xsl:value-of select="sitemap:priority"/>
                                </td>
                                <td>
                                    <xsl:value-of select="sitemap:changefreq"/>
                                </td>
                                <td>
                                    <xsl:value-of select="concat(substring(sitemap:lastmod, 0, 11),concat(' ', substring(sitemap:lastmod, 12, 5)))"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                </div>
                <div id="footer">
                    Developed by Universal Development <a href="http://universal-development.com/" title="Custom software development">http://universal-development.com/</a>, check with us if you need support at sitemapgenerator [at]
                    universal-development.com.
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>