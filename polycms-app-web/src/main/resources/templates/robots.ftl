User-agent: *
Allow: /

Sitemap: ${schema}://${domain}/sitemap.xml
Host: ${domain}

Disallow: /debug
Disallow: /*/debug

Disallow: /admin
Disallow: /admin*
Disallow: /jmx
Disallow: /jmx*
Disallow: /cgi-bin
Disallow: /account/not_my_account
Disallow: /oauth
Disallow: /1/oauth
Disallow: /i/streams
Disallow: /i/hello
Disallow: /wp-content/uploads/users/*/
Disallow: /cart
Disallow: /cart/purchase
Disallow: /cart/order

# Wait 1 second between successive requests. See ONBOARD-2698 for details.
Crawl-delay: 1