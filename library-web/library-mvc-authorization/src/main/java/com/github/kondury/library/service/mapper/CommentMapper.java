package com.github.kondury.library.service.mapper;

import com.github.kondury.library.model.Comment;
import com.github.kondury.library.service.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    CommentDto commentToCommentDto(Comment comment);
}
