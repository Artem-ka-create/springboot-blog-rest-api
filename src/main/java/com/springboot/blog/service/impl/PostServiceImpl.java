package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;
    private CategoryRepository categoryRepository;



    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper=mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Category","id", postDto.getCategoryId()));
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);
        return mapToDto(newPost);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        Page<Post> posts = postRepository.findAll(pageable);

        //get content from page
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content =  listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        return mapToDto(postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post", "id", id)));
    }

    @Override
    public PostDto updatePost(PostDto postDto,long id) {

        Post post = postRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Post", "id", id));
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("Category", "id", id));
        post.setCategory(category);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);

        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Post", "id", id));

        postRepository.delete(post);
    }

    @Override
    public List<PostDto> findByCategoryId(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category","Id",categoryId));
        List <PostDto> postList = postRepository.findByCategoryId(categoryId).stream().map((post) ->
                mapToDto(post)).collect(Collectors.toList());
        return postList;
    }

    private PostDto mapToDto(Post post){

        PostDto postDto = mapper.map(post,PostDto.class);
        System.out.println("HELLOOO");

        System.out.println(post);
        System.out.println(postDto);
//        PostDto postdto = new PostDto();
//        postdto.setId(post.getId());
//        postdto.setDescription(post.getDescription());
//        postdto.setTitle(post.getTitle());
//        postdto.setContent(post.getContent());
        return postDto;
    }
    private Post mapToEntity(PostDto postDto){

        Post post = mapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
