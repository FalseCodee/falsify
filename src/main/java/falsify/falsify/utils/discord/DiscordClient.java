package falsify.falsify.utils.discord;

public class DiscordClient {
//    private Core core;
//    public DiscordClient() {
//        CompletableFuture.runAsync(() -> {
//            try(CreateParams params = new CreateParams()) {
//                params.setClientID(700774561511374869L);
//                params.setFlags(CreateParams.getDefaultFlags());
//
//                try(Core temp = new Core(params)) {
//                    this.core = temp;
//
//                    try(Activity activity = new Activity()) {
//                        activity.setDetails("Legacy Client > u");
//                        activity.setState("winning");
//
//                        core.activityManager().updateActivity(activity);
//                    }
//                    while(true)
//                    {
//                        core.runCallbacks();
//                        try
//                        {
//                            // Sleep a bit to save CPU
//                            Thread.sleep(16);
//                        }
//                        catch(InterruptedException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    public static Activity makeActivity(String description) {
//        try(Activity activity = new Activity()) {
//            activity.setDetails(description);
//            activity.setState("winning");
//
//            return activity;
//        }
//    }
//
//    public void updateActivity(Activity activity) {
//        core.activityManager().updateActivity(activity);
//    }
}
