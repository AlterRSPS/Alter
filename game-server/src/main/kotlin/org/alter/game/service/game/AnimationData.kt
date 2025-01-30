import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AnimationData @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("walkAnim") val walkAnim: Int,
    @JsonProperty("runAnim") val runAnim: Int,
    @JsonProperty("readyAnim") val readyAnim: Int,
    @JsonProperty("turnAnim") val turnAnim: Int,
    @JsonProperty("walkAnimBack") val walkAnimBack: Int,
    @JsonProperty("walkAnimLeft") val walkAnimLeft: Int,
    @JsonProperty("walkAnimRight") val walkAnimRight: Int,
    @JsonProperty("accurateAnim") val accurateAnim: Int,
    @JsonProperty("accurateSound") val accurateSound: Int,
    @JsonProperty("aggressiveAnim") val aggressiveAnim: Int,
    @JsonProperty("aggressiveSound") val aggressiveSound: Int,
    @JsonProperty("controlledAnim") val controlledAnim: Int,
    @JsonProperty("controlledSound") val controlledSound: Int,
    @JsonProperty("defensiveAnim") val defensiveAnim: Int,
    @JsonProperty("defensiveSound") val defensiveSound: Int,
    @JsonProperty("blockAnim") val blockAnim: Int
)
