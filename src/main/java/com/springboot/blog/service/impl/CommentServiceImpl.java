package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BloxAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);
         // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));
        //set post to entity
        comment.setPost(post);

        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        //convert each element to dto
        return comments.stream().map((comment)-> mapToDto(comment))
                .collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));
        //retrieve COMMENT BY ID

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id", commentId));

        if(!comment.getPost().equals(post)){
            throw new BloxAPIException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(long postId,long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id", commentId));

        if(!comment.getPost().equals(post)){
            throw new BloxAPIException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());

        return mapToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment","id", commentId));

        if(!comment.getPost().equals(post)){
            throw new BloxAPIException(HttpStatus.BAD_REQUEST,"Comment doesnot belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = mapper.map(comment,CommentDto.class);

//        CommentDto commentDto = new CommentDto();
//        commentDto.setName(comment.getName());
//        commentDto.setId(comment.getId());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());

        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto,Comment.class);
//        Comment comment = new Comment();
//
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }
}
