package cz.cvut.fel.zan.macham29_zan.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FootballApi {
    @GET("v4/competitions/PL/matches")
    suspend fun getMatches(
        @Header("X-Auth-Token") token: String
    ): MatchResponse

    @GET("v4/competitions/PL/standings")
    suspend fun getStandings(
        @Header("X-Auth-Token") token: String
    ): StandingsResponse

    @GET("v4/matches/{id}")
    suspend fun getAfterMatchDetail(
        @Path("id") matchId: Int,
        @Header("X-Auth-Token") token: String
    ): MatchDetailResponse
}
