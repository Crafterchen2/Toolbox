# Toolbox
## What is this?
This is a collection of mini programs written in java, using my "Utilities" framework. Some *may* be more useless than others.
## Toolbox
The toolbox is the collection of several tools, where a tool is a small program using the "Utilities" framework.
You can select the tool that you would like to open, and it opens in a new tab. You then can use the tool in the toolbox
main window, reset the tool, or eject the tool into its own window. The main toolbox window has controls to reset, close
and manage opened tools. It is also possible to start the toolbox in "discrete"- mode. this mode is just a singular
stand-alone tool in its own window. To engage this mode, enter as many names of tools as you want to open as start parameters.
## Utilities framework
The utilities framework has two core interfaces: **Utility** and **Resettable**. One core feature of the framework is the
resettability of every utility. To achieve perfect resettability of a utility, every non-final piece of data should be configured
in the `resetCode()` method of the "Resettable" interface. The method `canReset()` controls, whether `resetCode()` may be executed.
To initiate a reset, only the method `reset()` should be called (`resetCode()` is only public as an implementation side effect).
After a reset, the state of the utility should be the same as the state directly after object creation.
It's the job of the Utility interface to privide the name of the utility, a way to access the component represented by this utility and
how new instances of this type of utility should be created. Often, returning `this` for the `getComponent()` method suffices,
when the implementing class also extends `java.awt.Component` in some way.
### More info
This project is written using openjdk-23 and therefore produces a jar with the file format 67, thus requiring a very
up-to-date version of the java runtime.