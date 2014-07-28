package nsl.stg.uiautomator.cmds;

import java.util.Arrays;

/**
 * Entry point into the uiautomator command line
 *
 * This class maintains the list of sub commands, and redirect the control into it based on the
 * command line arguments. It also prints out help arguments for each sub commands.
 *
 * To add a new sub command, implement {@link Command} and add an instance into COMMANDS array
 */
public class MyLauncher {

	/**
	 * A simple abstraction class for supporting generic sub commands
	 */
	public static abstract class Command {
		private String mName;

		public Command(String name) {
			mName = name;
		}

		/**
		 * Returns the name of the sub command
		 * @return
		 */
		public String name() {
			return mName;
		}

		/**
		 * Returns a one-liner of the function of this command
		 * @return
		 */
		public abstract String shortHelp();

		/**
		 * Returns a detailed explanation of the command usage
		 *
		 * Usage may have multiple lines, indentation of 4 spaces recommended.
		 * @return
		 */
		public abstract String detailedOptions();

		/**
		 * Starts the command with the provided arguments
		 * @param args
		 */
		public abstract void run(String args[]);
	}

	public static void main(String[] args) {
		// show a meaningful process name in `ps`
		// Process.setArgV0("uiautomator");
		if (args.length >= 1) {
			Command command = findCommand(args[0]);
			if (command != null) {
				String[] args2 = {};
				if (args.length > 1) {
					// consume the first arg
					args2 = Arrays.copyOfRange(args, 1, args.length);
				}
				command.run(args2);
				return;
			}
		}
		HELP_COMMAND.run(args);
	}

	private static Command findCommand(String name) {
		for (Command command : COMMANDS) {
			if (command.name().equals(name)) {
				return command;
			}
		}
		return null;
	}

	private static Command HELP_COMMAND = new Command("help") {
		@Override
		public void run(String[] args) {
			System.err.println("Usage: uiautomator <subcommand> [options]\n");
			System.err.println("Available subcommands:\n");
			for (Command command : COMMANDS) {
				String shortHelp = command.shortHelp();
				String detailedOptions = command.detailedOptions();
				if (shortHelp == null) {
					shortHelp = "";
				}
				if (detailedOptions == null) {
					detailedOptions = "";
				}
				System.err.println(String.format("%s: %s", command.name(), shortHelp));
				System.err.println(detailedOptions);
			}
		}

		@Override
		public String detailedOptions() {
			return null;
		}

		@Override
		public String shortHelp() {
			return "displays help message";
		}
	};

	private static Command[] COMMANDS = new Command[] { HELP_COMMAND, new MyRunTestCommand(), };
}
