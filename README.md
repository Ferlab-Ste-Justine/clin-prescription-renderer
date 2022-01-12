# clin-prescription-renderer

This API generates PDF of prescriptions.

# Endpoints

|url|description|
|-|-|
|GET http://localhost:8080/pdf/:serviceRequestId?lang=fr|Generate and download PDF|
|GET http://localhost:8080/render/:serviceRequestId?lang=fr|Render the prescription as HTML, only with *-Dspring.profiles.active=dev*|

*Note: `lang` param is optional and **fr** is the default lang.*

During development, it will also be easier to have the IDE in rebuild/relaunch project automatically when files change like HTML ...

# Security

Endpoints require `Authorization` bearer token in header.

# Limitations

Due to the PDF export there are some limitations and the main rule is to have standalone HTML page with no static resources to load + use basic HTML/CSS:
- all CSS must be included inside the HTML template through `<style>` tag or inline `style=""`
- all images must be loaded via `ResourceService` as base64 string and passed as params to the Thymeleaf template
- em/rem not supported => use px units
- flexbox not supported => use table structures
- avoid `<p>` the start/end margin can't be changed, use line-height and `<br/>` instead
- input checkbox doesn't render well in PDF, has been replaced by a GIF image
