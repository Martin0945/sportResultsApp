package cz.cvut.fel.zan.macham29_zan.api


data class MatchResponse(
    val matches: List<MatchDto>
)

data class MatchDto(
    val id: Int,
    val utcDate: String,
    val status: String,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val score: ScoreDto
)

data class TeamDto(
    val name: String
)

data class ScoreDto(
    val fullTime: FullTimeDto,
    val halfTime: FullTimeDto?
)

data class FullTimeDto(
    val home: Int?,
    val away: Int?
)

data class StandingsResponse(
    val standings: List<StandingDto>,
    val competition: CompetitionDto
)

data class CompetitionDto(
    val emblem: String?
)

data class StandingDto(
    val type: String,
    val table: List<TableRecordDto>
)

data class TableRecordDto(
    val position: Int,
    val team: TeamInfoDto,
    val playedGames: Int,
    val points: Int
)

data class TeamInfoDto(
    val id: Int,
    val name: String,
    val crest: String?
)

data class MatchDetailResponse(
    val id: Int,
    val status: String,
    val matchday: Int?,
    val venue: String?,
    val referees: List<RefereeDto>?,
    val homeTeam: TeamDetailDto,
    val awayTeam: TeamDetailDto,
    val score: ScoreDto
)

data class RefereeDto(
    val name: String,
    val type: String
)

data class TeamDetailDto(
    val id: Int,
    val name: String,
    val crest: String
)
