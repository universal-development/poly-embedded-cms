<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="${schema}://${domain}/sitemap.xsl"?>

<#setting date_format="yyyy-MM-dd'T'HH:mm-hh:00">
<urlset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9
	http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd"
        xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">

    <#list items as item>
    <url>
        <loc>${schema}://${domain}/${item._id}</loc>
        <lastmod>${item.date?number_to_datetime}</lastmod>
        <changefreq>daily</changefreq>
        <priority>0.5f</priority>
    </url>
    </#list>
</urlset>