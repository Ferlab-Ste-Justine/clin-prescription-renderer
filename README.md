# clin-prescription-renderer

This API generates PDF for prescriptions.

# Endpoints

Generate and download PDF
```

http://localhost:8080/pdf/:serviceRequestId
```
In order to help the development, an endpoint is available with `dev` profile.
It will render the prescription as an HTML page without downloading.
```

http://localhost:8080/render/:serviceRequestId
```

*Note: enable `dev` profile with the following: -Dspring.profiles.active=dev*

# Security

Endpoints require `Authorization` bearer token in header.

# Limitations

It's not possible to import external files in Thymeleaf templates due to the PDF export that won't be able to locate them.
Workaround is to apply the following rules:
- all CSS must be included inside the HTML template through `<style>` tag or inline `style=""`
- all images must be loaded via `ResourceService` as base64 string and passed as params to the Thymeleaf template
 
