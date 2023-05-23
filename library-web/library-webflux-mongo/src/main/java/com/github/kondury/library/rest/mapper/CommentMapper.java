package com.github.kondury.library.rest.mapper;

import com.github.kondury.library.domain.Comment;
import com.github.kondury.library.rest.dto.CommentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto commentToCommentDto(Comment comment);
}
