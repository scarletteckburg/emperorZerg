import bwapi.*;

public class ProtossStrat extends Routine {
    private final Game game;
    private final Player self;
    private final enemyChalkBoard enemy;
    private Selector selector;

    @Override
    public void start(){
        super.start();
    }

    public void reset() {
        selector = new Selector();
        start();
    }

    public ProtossStrat(ChalkBoard info, Selector selector){
        super();
        this.game = info.game;
        this.self = info.pcb.self;
        this.enemy = info.ecb;
        this.selector = selector;
    }


    public void act(ChalkBoard info) {
        if (enemy.race != Race.Protoss) {
            System.out.println("VS PROTOSS FAIL");
            fail();
        }
        selector.addRoutine(new NinePool(info, new Sequencer()));
        selector.addRoutine(new MidgameBuilds(info));
        int drones = 0;
        for(Unit unit : self.getUnits()){
            UnitType unitType = unit.getType();
            if(unitType.isWorker()) {
                drones++;
            }
        }
        if(self.supplyTotal() - self.supplyUsed() < 4){
            selector.addRoutine(new MorphUnit(info, UnitType.Zerg_Overlord, 1, false));
        }
        if(drones < 14){
            selector.addRoutine(new MorphUnit(info, UnitType.Zerg_Drone, 1, false));
        }
        selector.addRoutine(new MorphUnit(info, UnitType.Zerg_Zergling, 1, false));
        selector.act(info);
        if(selector.isFailure() || selector.isSuccess()){
            reset();
        }
    }
}
