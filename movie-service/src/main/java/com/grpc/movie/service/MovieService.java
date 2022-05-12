package com.grpc.movie.service;

import com.grpc.movie.repository.MovieRepo;
import com.mrb.grpc.movie.MovieDto;
import com.mrb.grpc.movie.MovieSearchRequest;
import com.mrb.grpc.movie.MovieSearchResponse;
import com.mrb.grpc.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepo movieRepo;

    @Override
    public void getMovie(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtolist=this.movieRepo.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setRatting(movie.getRating())
                        .setYear(movie.getYear()).build()).collect(Collectors.toList());
        responseObserver.onNext(MovieSearchResponse.newBuilder().addAllMovie(movieDtolist).build());
        responseObserver.onCompleted();

    }
}
