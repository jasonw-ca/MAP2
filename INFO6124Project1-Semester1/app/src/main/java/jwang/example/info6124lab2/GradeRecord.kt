package jwang.example.info6124lab2

import java.io.Serializable

data class GradeRecord (var courseName:String?, var gradeType:String?, var fullGrade: String?,
                        var receivedGrade: String?, var percentage: String?
                        ): Serializable