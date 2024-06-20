# Blog Application API
This RESTful API provides endpoints to manage blogs and their associated tags.

### Base URL
All endpoints are relative to the base URL:

http://localhost:8080/api/blogs

#
Returns a list of all blogs.
* GET http://localhost:8080/api/blogs

Returns a specific blog by its ID.
* GET http://localhost:8080/api/blogs/{id}

Creates a new blog.
* POST http://localhost:8080/api/blogs

Updates an existing blog by its ID.
* PUT http://localhost:8080/api/blogs/{id}

Deletes a blog by its ID.
* DELETE http://localhost:8080/api/blogs/{id}

Adds a tag to a specific blog.
* POST http://localhost:8080/api/blogs/{id}/add-tag?tag=tutorial

Removes a tag from a specific blog.
* DELETE http://localhost:8080/api/blogs/{id}/remove-tag?tag=spring

Returns a list of blogs that have the specified tag.
* GET http://localhost:8080/api/blogs/tags/java


