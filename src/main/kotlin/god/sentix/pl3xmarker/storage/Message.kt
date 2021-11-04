package god.sentix.pl3xmarker.storage

class Message {

    companion object {

        const val PREFIX = "<gray>[<gradient:#C028FF:#5B00FF>Pl3xMap-Marker</gradient>]</gray> "

        const val NO_PERM = "$PREFIX<red>No permission.</red>"

        const val NO_PLAYER = "$PREFIX<red>Not a player.</red>"

        const val NUMBER_EXC = "$PREFIX<red>ID has to be a number.</red>"

        const val EMPTY = "$PREFIX<red>No markers set.</red>"

    }

}