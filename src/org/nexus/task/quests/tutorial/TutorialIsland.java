package org.nexus.task.quests.tutorial;


import java.awt.Graphics2D;

import org.nexus.task.ActionTask;
import org.nexus.task.Task;
import org.nexus.task.quests.tutorial.sections.*;
import org.osbot.rs07.Bot;
import org.osbot.rs07.script.MethodProvider;



public final class TutorialIsland extends Task {

    private final TutorialSection rsGuideSection = new RuneScapeGuideSection();
    private final TutorialSection survivalSection = new SurvivalSection();
    private final TutorialSection cookingSection = new CookingSection();
    private final TutorialSection questSection = new QuestSection();
    private final TutorialSection miningSection = new MiningSection();
    private final TutorialSection fightingSection = new FightingSection();
    private final TutorialSection bankSection = new BankSection();
    private final TutorialSection priestSection = new PriestSection();
    private final TutorialSection wizardSection = new WizardSection();


    @Override
    public MethodProvider exchangeContext(Bot bot) {
    	rsGuideSection.exchangeContext(bot);
        survivalSection.exchangeContext(bot);
        cookingSection.exchangeContext(bot);
        questSection.exchangeContext(bot);
        miningSection.exchangeContext(bot);
        fightingSection.exchangeContext(bot);
        bankSection.exchangeContext(bot);
        priestSection.exchangeContext(bot);
        wizardSection.exchangeContext(bot);
    	return super.exchangeContext(bot);
    }

    @Override
    public final int onLoop() throws InterruptedException {
    	log(getTutorialSection());
        switch (getTutorialSection()) {
            case 0:
            case 1:
                rsGuideSection.onLoop();
                break;
            case 2:
            case 3:
                survivalSection.onLoop();
                break;
            case 4:
            case 5:
                cookingSection.onLoop();
                break;
            case 6:
            case 7:
                questSection.onLoop();
                break;
            case 8:
            case 9:
                miningSection.onLoop();
                break;
            case 10:
            case 11:
            case 12:
                fightingSection.onLoop();
                break;
            case 14:
            case 15:
                bankSection.onLoop();
                break;
            case 16:
            case 17:
                priestSection.onLoop();
                break;
            case 18:
            case 19:
            case 20:
                wizardSection.onLoop();
                break;
        }
        return 200;
    }

    private int getTutorialSection() {
        return getConfigs().get(406);
    }
    

    private boolean isTutorialIslandCompleted() {
        return getWidgets().getWidgetContainingText("Tutorial Island Progress") == null;
    }
    public boolean isFinished() {
    	return isTutorialIslandCompleted();
    }

	@Override
	public void onExit() {
		log("Tut island is completed");
	}

}
