package today.flux.addon.api.command;


import today.flux.module.Command;

public abstract class AddonCommand {
    
    protected final String name, description;
    
    protected final String[] syntax;

    
    private final Command nativeCommand;

    public AddonCommand(String name, String description, String... syntax) {
        this.name = name;
        this.description = description;
        this.syntax = syntax;

        this.nativeCommand = new Command(name, description, syntax) {
            @Override
            public void execute(String[] args) {
                onExecute(args);
            }
        };
    }

    public abstract void onExecute(String[] args);

	public Command getNativeCommand() {
		// TODO 自动生成的方法存根
		return nativeCommand;
	}

	public Object getName() {
		// TODO 自动生成的方法存根
		return name;
	}
}
