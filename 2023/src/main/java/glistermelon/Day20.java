package glistermelon;

import org.apache.commons.math3.util.ArithmeticUtils;

import java.io.IOException;
import java.util.*;

public class Day20 extends DaySolver {

    public Day20() throws IOException {
        super(20);
    }

    RepeaterModule broadcaster;
    List<Module> modules;

    public void runSharedLogic() {}

    public String solvePart1() {

        resetSystem();

        int lows = 0, highs = 0;
        for (int press = 0; press < 1000; press++) {
            lows++;
            List<Signal> queue = new ArrayList<>();
            queue.add(new Signal(broadcaster, null, false));
            while (!queue.isEmpty()) {
                Signal signal = queue.removeFirst();
                List<Signal> relay = signal.target.processPulse(signal.sender, signal.high);
                queue.addAll(relay);
                for (Signal newSignal : relay) {
                    if (newSignal.high) highs++;
                    else lows++;
                }
            }
        }

        return String.valueOf(lows * highs);

    }

    public String solvePart2() {

        resetSystem();

        List<Long> periods = new ArrayList<>();

        for (Module chainStart : broadcaster.targets) {

            StringBuilder binary = new StringBuilder();
            FlipModule node = (FlipModule)chainStart;
            do {
                binary.append(
                        node.targets.stream().anyMatch(t -> t instanceof ConjModule) ? '1' : '0'
                );
                var targets = node.targets;
                node = null;
                for (Module next : targets) {
                    if (next instanceof FlipModule) {
                        node = (FlipModule)next;
                        break;
                    }
                }
            } while (node != null);

            periods.add(Long.parseLong(binary.reverse().toString(), 2));

        }

        long lcm = 1;
        for (long period : periods) lcm = ArithmeticUtils.lcm(lcm, period);

        return String.valueOf(lcm);

    }

    private void resetSystem() {

        broadcaster = new RepeaterModule();
        modules = new ArrayList<>();

        Set<String> allNames = new HashSet<>();
        Set<String> nonDummyNames = new HashSet<>();

        for (String line : getPuzzleInputLines()) {

            String[] split = line.replace(" -> ", " ")
                    .replaceAll(",", "").split(" ");

            String name = split[0];

            List<String> targets = new ArrayList<>(Arrays.stream(split).toList());
            targets.removeFirst();

            if (name.equals("broadcaster")) {
                broadcaster.setName(name);
                broadcaster.addTargetNames(targets);
                continue;
            }

            allNames.addAll(targets);

            char typeChar = name.charAt(0);
            name = name.substring(1);
            Module module = typeChar == '%' ? new FlipModule() : new ConjModule();
            module.addTargetNames(targets);
            module.setName(name);
            modules.add(module);

            for (String target : targets) {
                Module.addInputName(name, target);
            }

            nonDummyNames.add(name);

        }

        for (String name : allNames) {
            if (nonDummyNames.contains(name)) continue;
            DummyModule module = new DummyModule();
            module.setName(name);
        }

        for (Module m : modules) {
            m.loadPending();
            m.initialize();
        }
        broadcaster.loadPending();

    }

    private record Signal(Module target, Module sender, boolean high) {}

    private abstract static class Module {

        protected final List<Module> targets = new ArrayList<>();
        private final List<String> pendingTargets = new ArrayList<>();

        protected final List<Module> inputs = new ArrayList<>();
        static private final Map<String, List<String>> pendingInputs = new HashMap<>();

        protected String name;

        private final static Map<String, Module> instances = new HashMap<>();

        public void addTargetNames(List<String> names) {
            pendingTargets.addAll(names);
        }

        static public void addInputName(String inputName, String moduleName) {
            List<String> names = pendingInputs.getOrDefault(moduleName, new ArrayList<>());
            names.add(inputName);
            pendingInputs.put(moduleName, names);
        }

        public void setName(String name) {
            instances.put(name, this);
            this.name = name;
        }

        public void loadPending() {
            for (String name : pendingTargets) targets.add(instances.get(name));
            pendingTargets.clear();
            if (pendingInputs.containsKey(name)) {
                for (String inputName : pendingInputs.get(name)) {
                    inputs.add(instances.get(inputName));
                }
                pendingInputs.remove(name);
            }
        }

        public void initialize() {}

        public List<Signal> processPulse(Module sender, boolean high) {

//            System.out.print(sender == null ? "button" : sender.name);
//            System.out.print(" -");
//            System.out.print(high ? "high" : "low");
//            System.out.print("-> " + this.name);
//            System.out.println();

            Boolean output = handlePulse(sender, high);
            List<Signal> signals = new ArrayList<>();
            if (output != null) {
                for (Module target : targets) signals.add(new Signal(target, this, output));
            }
            return signals;
        }

        protected abstract Boolean handlePulse(Module sender, boolean high);

    }

    private static class ConjModule extends Module {

        final Map<Module, Boolean> memory = new HashMap<>();

        public void initialize() {
            for (Module input : inputs) memory.put(input, false);
        }

        protected Boolean handlePulse(Module sender, boolean high) {
            memory.put(sender, high);
            return !memory.values().stream().allMatch(Boolean::booleanValue);
        }

    }

    private static class FlipModule extends Module {

        boolean state = false;

        protected Boolean handlePulse(Module sender, boolean high) {
            if (high) return null;
            state = !state;
            return state;
        }

    }

    private static class RepeaterModule extends Module {

        protected Boolean handlePulse(Module sender, boolean high) {
            return high;
        }

    }

    private static class DummyModule extends Module {
        protected Boolean handlePulse(Module sender, boolean high) {
            return null;
        }
    }

}