package vm.computer.components;

import vm.computer.Machine;
import vm.computer.SquareSynthesizer;

public class Computer extends ComponentBase {
	private SquareSynthesizer synthesizer = new SquareSynthesizer();
	private boolean soundAvailable = false;

	public Computer(Machine machine, String address) {
		super(machine, address, "computer");

		if(synthesizer.checkDataLine()) {
			soundAvailable = true;
			System.out.println("ХХОБА!");
		} else {
			System.err.println("Блядь!");
		}

		this.machine = machine;
	}

	@Override
	public void pushProxyFields() {
		super.pushProxyFields();

		machine.lua.pushJavaFunction(args -> {
			rawBeep(args.checkInteger(1), (long) (args.checkNumber(2) * 1000));

			return 0;
		});
		machine.lua.setField(-2,  "beep");

//		machine.lua.pushJavaFunction(args -> {
//			machine.boot();
//
//			return 0;
//		});
//		machine.lua.setField(-2,  "start");
//
//		machine.lua.pushJavaFunction(args -> {
//			machine.shutdown(true);
//
//			return 0;
//		});
//		machine.lua.setField(-2,  "stop");

		machine.lua.pushJavaFunction(args -> {
			machine.lua.pushBoolean(true);

			return 1;
		});
		machine.lua.setField(-2,  "isRunning");
	}

	public void rawBeep(int frequency, long duration) {
		if(soundAvailable)
			synthesizer.tone(frequency, (int) duration);
		else
			System.err.println("Блядь, беды с SDL");

		try {
			Thread.sleep(duration);
		}
		catch (InterruptedException e) {}
		finally {
			try {
				Thread.sleep(50);
				if(soundAvailable)
					synthesizer.sdl.stop();
			}
			catch (InterruptedException e) {}
		}
	}
}
