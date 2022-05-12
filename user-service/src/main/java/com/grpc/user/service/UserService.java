package com.grpc.user.service;

import com.grpc.user.repository.UserRepo;
import com.mrb.grpc.common.Genre;

import com.mrb.grpc.service.UserGenreUpdateRequest;
import com.mrb.grpc.service.UserResponse;
import com.mrb.grpc.service.UserSearchRequest;
import com.mrb.grpc.service.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepo userRepo;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {

        UserResponse.Builder builder=UserResponse.newBuilder();
        this.userRepo.findById(request.getLoginId()).
                ifPresent(user -> {
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    @Override
    @Transactional ///For automatic updates
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {


        UserResponse.Builder builder= UserResponse.newBuilder();
        this.userRepo.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
