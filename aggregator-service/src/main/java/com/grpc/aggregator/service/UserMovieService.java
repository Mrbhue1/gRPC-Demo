package com.grpc.aggregator.service;

import com.grpc.aggregator.dto.RecommendedMovie;
import com.grpc.aggregator.dto.UserGenre;
import com.mrb.grpc.common.Genre;
import com.mrb.grpc.movie.MovieSearchRequest;
import com.mrb.grpc.movie.MovieSearchResponse;
import com.mrb.grpc.movie.MovieServiceGrpc;
import com.mrb.grpc.service.UserGenreUpdateRequest;
import com.mrb.grpc.service.UserResponse;
import com.mrb.grpc.service.UserSearchRequest;
import com.mrb.grpc.service.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<RecommendedMovie> getMovieUserSuggestion(String loginId){
        UserSearchRequest userSearchRequest=UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse=this.userStub.getUserGenre(userSearchRequest);

        MovieSearchRequest movieSearchRequest= MovieSearchRequest.newBuilder().setGenre(userResponse.getGenre()).build();
        MovieSearchResponse movieSearchResponse=this.movieStub.getMovie(movieSearchRequest);

        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto -> new RecommendedMovie(movieDto.getTitle(),movieDto.getYear(),movieDto.getRatting()))
                .collect(Collectors.toList());
    }
    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest=UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase())).build();
        UserResponse userResponse=this.userStub.updateUserGenre(userGenreUpdateRequest);
    }
}
