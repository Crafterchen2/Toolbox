# Toolbox
## What is this?
This is a collection of mini programs written in java, using my "Utilities" framework. Some *may* be more useless than others.
## Toolbox
The Toolbox is the collection of several Utilities, where a Utility is a small program using the "Utilities" framework.
You can select the tool that you would like to open, and it opens in a new tab. You then can use the tool in the toolbox
main window, reset the tool, or eject the tool into its own window. The main toolbox window has controls to reset, close
and manage opened tools. It is also possible to start the Toolbox in "discrete"- mode. this mode is just a singular
stand-alone tool in its own window. to engage this mode, enter as many names of tools as you want to open as start parameters.
## Utilities framework
The Utilities framework has two core interfaces: **Utility** and **Resettable**. One core feature of the framework is the
resettability of every utility. To achieve perfect resettability of a utility, every non-final piece of data should be configured
in the `resetCode()` method of the "Resettable" interface. The method `canReset()` controls, whether `resetCode()` may be executed.
To initiate a reset, only the method `reset()` should be called (`resetCode()` is only public as an implementation side effect).
It's the job of the Utility interface to privide the name of the utility, a way to access the component represented by this utitlity
how new instances of this type of utility should be created. often returning `this` for the `getComponent()` method suffices,
when the implementing class also extends component in some way. It's recommended but not required for the name to be formatted using UTF-8,
as that is supported by most terminals.
### More info
This project is written using openjdk-22 and therefore produces a jar with the file format 66, thus requiring a very
up-to-date version of the java runtime.