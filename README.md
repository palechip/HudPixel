# HudPixelReloaded
### An unofficial client-side Minecraft modification designed for the Hypixel Network.

 1. Check out the original version by  [palechip](https://github.com/palechip/HudPixel).
 2. Check out the [extended version](https://github.com/unaussprechlich/HudPixelExtended) by [Eladkay](https://github.com/Eladkay) and [unaussprechlich](https://github.com/unaussprechlich).

#### Contributing
This mod is open source and contributions are always welcome.

#### How to build/compile the mod
 1. Make sure you have the Java Development Kit (jdk) installed
 2. Download the sources
 3. Open a command line in the main directory (where this readme is located)
 4. Run `gradlew build`
 5. Grab your build in `build\libs`

#### How to setup your IDE to work with this mod 
Use this [YoutubeGuide](https://www.youtube.com/watch?v=PfmlNiHonV0) provided by djsmegku.
 1. Make sure you have the Java Development Kit (jdk) installed
 2. Download the sources (ideally use a fork so you can do a pull request)
 3. Open a command line in the main directory (where this readme is located)
 4. Run `gradlew setupDecompWorkspace --refresh-dependencies`
 5. If you are using IntelliJ IDEA as IDE, run `gradlew idea`
 6. If you are using Eclipse as IDE, head to [files.minecraftforge.net](http://files.minecraftforge.net), download the source code of the used version of Forge, unzip it and copy the `eclipse` folder into the main directory. Then run `gradlew eclipse`.
 7. Open the project with your IDE (for Eclipse: Point your workspace to the `eclipse` folder)
 8. Make sure you are using the Java Conventions for Formatting the code, but tell it to avoid tabs. (This is especially important for bigger pull requests. If you don't know how to do it, I'll help you once you make a pull request.)
 9. To be able to test the mod on the server, configure the IDE to pass Minecraft the following arguments on start `--username YOURUSERNAMEOREMAIL --password YOURPASSWORD` Use the data you are using to log in to the Minecraft launcher.
