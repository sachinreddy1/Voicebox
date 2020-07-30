package com.sachinreddy.feature.data

object TestData {
    val artist1 = Artist(
        "1",
        "Artist Name",
        "test",
        "test1@test1.com",
        "120",
        "https://firebasestorage.googleapis.com/v0/b/collab-c4a6e.appspot.com/o/artists%2FcQcGlyqMi5SszPN4dBKhjR3WgG63?alt=media&token=99c3c99b-7485-43df-a0ec-4974cae1a227",
        null
    )
    val artist2 = Artist(
        "2",
        "Travis Scott",
        "laflame",
        "travisscott@gmail.com",
        "120",
        "https://i1.sndcdn.com/avatars-000701366305-hu9f0i-t500x500.jpg",
        "https://static.vecteezy.com/system/resources/previews/000/100/314/original/trendy-abstract-geometric-pattern-background-vector.jpg"
    )
    val artist3 = Artist(
        "3",
        "Future",
        "future",
        "future@gmail.com",
        "120",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQup0zJL8qAuVwcDDwk_k_qerMYxldb7cUeWw&usqp=CAU",
        "https://www.designyourway.net/blog/wp-content/uploads/2018/06/Seamless-Space-Pattern-by-J.jpg"
    )

    val songs_: List<Song> = listOf(
        Song(
            artist2,
            "Untitled",
            false
        ),
        Song(
            artist3,
            "Miss u bitch",
            false
        )
    )
}
