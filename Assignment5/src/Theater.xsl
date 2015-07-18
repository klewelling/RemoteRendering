<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <body>
                <h1>Theater: <xsl:value-of select="movietheater/@name"/></h1>
                <h4><xsl:value-of select="movietheater/location/@name"/></h4>
                <h4><xsl:value-of select="movietheater/location"/></h4>

                <xsl:for-each select="movietheater/movielisting/movie">

                    <h2><xsl:value-of select="@name"/></h2>
                    <h4>Duration: <xsl:value-of select="duration"/></h4>
                    <h4>Type: <xsl:value-of select="type"/></h4>


                    <table border="1">
                        <tr>
                            <th>Showtimes</th>
                        </tr>

                        <xsl:for-each select="times/showing">
                            <tr>
                                <xsl:choose>
                                    <xsl:when test=". > 1700">
                                        <td bgcolor="#FF0000">
                                            <xsl:value-of select="."/>
                                        </td>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <td>
                                            <xsl:value-of select="."/>
                                        </td>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </tr>
                        </xsl:for-each>

                    </table>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
