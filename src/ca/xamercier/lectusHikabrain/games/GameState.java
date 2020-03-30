package ca.xamercier.lectusHikabrain.games;

import org.bukkit.Bukkit;

import net.lectusAPI.MainLectusApi;

public enum GameState {
	
    WAITING(true),
    STARTING(false),
    RUNNING(false),
    FINISH(false);

    private static GameState status;
    private boolean canJoin;

    GameState(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public boolean canJoin() {
        return this.canJoin;
    }

    public static boolean isState(GameState check) {
        return status == check;
    }

    public static void setState(GameState state) {
        GameState.status = state;
		int servport = Bukkit.getServer().getPort();
		String port = Integer.toString(servport);
		MainLectusApi.getInstance().getSql().setState(port, state.toString());
    }

}
