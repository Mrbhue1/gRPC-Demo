package com.grpc.aggregator.controller;


import com.grpc.aggregator.dto.RecommendedMovie;
import com.grpc.aggregator.dto.UserGenre;
import com.grpc.aggregator.service.UserMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("user/{loginId}")
    public List<RecommendedMovie> getMovie(@PathVariable String loginId){
        return this.userMovieService.getMovieUserSuggestion(loginId);
    }

    @PutMapping("/user")
    public void setUserName(@RequestBody UserGenre userGenre){
        this.userMovieService.setUserGenre(userGenre);
    }
}
