package com.randioo.randioo_server_base;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		System.out.println("Hello World!");
		
//		GameMatcher gameMatcher = new GameMatcher();
//		gameMatcher.setThreadSize(5);
//		gameMatcher.setMatchHandler(new MatchHandler() {
//			
//			@Override
//			public boolean needWaitMatch(MatchInfo matchInfo) {
//				System.out.println("needWaitMatch");
//				return true;
//			}
//			
//			@Override
//			public void matchSuccess(MatchInfo matchInfo, MatchRule matchRule) {
//				System.out.println("matchSuccess");
//			}
//			
//			@Override
//			public boolean matchRule(MatchRule myMatchRule, MatchInfo otherMatchInfo) {
//				// TODO Auto-generated method stub\
//				System.out.println("matchRule");
//				return true;
//			}
//			
//			@Override
//			public void matchComplete(MatchInfo matchInfo) {
//				// TODO Auto-generated method stub
//				System.out.println("matchComplete");
//			}
//			
//			@Override
//			public MatchRule getAutoMatchRole(MatchInfo matchInfo) {
//				System.out.println("getAutoMatchRole");
//				MatchRule matchRule = new MatchRule();
//				matchRule.setMatchNPC(false);
//				matchRule.setMatchTarget(new Matcher());
//				matchRule.setPlayerCount(2);
//				matchRule.setWaitTime(5);
//				matchRule.setWaitUnit(TimeUnit.SECONDS);
//				return matchRule;
//			}
//			
//			@Override
//			public void destroyMatchInfo(MatchInfo matchInfo) {
//				// TODO Auto-generated method stub
//				System.out.println("destroyMatchInfo");
//			}
//			
//			@Override
//			public MatchInfo createMatchInfo(MatchRule matchRule) {
//				// TODO Auto-generated method stub
//				System.out.println("createMatchInfo");
//				return new MatchInfo();
//			}
//
//			@Override
//			public void cancelMatch(Matchable matchable) {
//				// TODO Auto-generated method stub
//				System.out.println("cancel match success");
//			}
//
//			@Override
//			public void changeStartMatcher(Matchable originStarter, Matchable newStarter) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void waitClick(MatchInfo matchInfo, int clickCount) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		Matcher matcher = new Matcher();
//    
//		MatchRule matchRule = new MatchRule();
//		matchRule.setMatchNPC(false);
//		matchRule.setMatchTarget(matcher);
//		matchRule.setPlayerCount(2);
//		matchRule.setWaitTime(10);
//		matchRule.setWaitUnit(TimeUnit.SECONDS);
//		
//		gameMatcher.matchRole(matchRule);
//		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		gameMatcher.cancelMatch(matcher);
//		
//		while(true){
//			Scanner in = new Scanner(System.in);
//			String line = in.nextLine();
//			System.out.println("");
//		}
    }
    
}
