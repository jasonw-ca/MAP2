package jwang.example.info6130project1

data class APIFormat(
        var latest_photos: ArrayList<Photo>?
)

data class Photo (
        var sol: Int,
        var camera: Camera,
        var img_src: String,
        var rover: Rover,
        var earth_date: String
        )

data class Camera (
        var name: String
)

data class Rover (
        var name: String
)