package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@Tag(
        name = "CRUD REST APIs FOR POST RESOURCE"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create blog post
    @Operation(
            summary = "Create post rest api",
            description = "CreatePost rest api is userd to save post to DB"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP STATUS CREATED 201"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }


    // get all potsts rest api
    @Operation(
            summary = "Get all posts rest api",
            description = "Get ALL POst rest api is userd to get all posts from DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK 200"
    )
    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts (
                @RequestParam (value="pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                @RequestParam (value="pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                @RequestParam (value="sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                @RequestParam (value="sortDir", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortDir
            ){
        return postService.getAllPosts(pageNo, pageSize,sortBy,sortDir);
    }


    @Operation(
            summary = "Get post rest api",
            description = "Get POst rest api is userd to get post from DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK 200"
    )
    @GetMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable (name = "id") long id){

        return ResponseEntity.ok(postService.getPostById(id));
    }



    //updatePost By id rest api
    @Operation(
            summary = "update post rest api",
            description = "update POst rest api is userd to update post from DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK 200"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost ( @Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "delete post rest api",
            description = "delete POst rest api is userd to delete post from DB"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP STATUS OK 200"
    )
    @SecurityRequirement(name = "Bear Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted", HttpStatus.OK);
    }

    @GetMapping("/api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDto>> getAllPostsByCategoryId(@PathVariable("id") long categoryId){
        List<PostDto> postDtos = postService.findByCategoryId(categoryId);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }


}
