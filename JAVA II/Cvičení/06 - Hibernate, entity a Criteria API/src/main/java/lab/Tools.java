package lab;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tools {

	public static final Random RANDOM = new Random();

	public static final List<String> FIRST_NAMES = List.of("Petr", "Marie", "Jan", "Jana", "Tomáš", "Kateřina", "Lukáš",
			"Tereza", "Martin", "Veronika", "David", "Eva", "Jakub", "Lucie", "Michal", "Anna", "Adam", "Monika", "Tom",
			"Klára", "Robert", "Kristýna", "Marek", "Simona", "Filip", "Petra", "Ondřej", "Lenka", "Matěj", "Markéta",
			"Pavel", "Hana", "Jakub", "Adéla", "Daniel", "Barbora", "Lukáš", "Eliška", "Josef");
	public static final List<String> LAST_NAMES = List.of("Novák", "Svoboda", "Novotný", "Dvořák", "Černý",
			"Procházková", "Kučera", "Veselý", "Horák", "Němec", "Pokorný", "Mareš", "Pospíšilová", "Hájek", "Jelínek",
			"Král", "Růžička", "Beneš", "Fiala", "Sedláček", "Kříž", "Němcová", "Vlček", "Kolář", "Bartoš", "Bílý",
			"Veselá", "Kovařík", "Havelka", "Malý", "Urban", "Kopecký", "Vlach", "Šimek", "Konečný", "Doležal",
			"Šťastný", "Kopečná", "Holub", "Pospíchal");

	public static final List<String> STREETS = List.of("Panská", "Hlavní", "Ulice Nová", "Náměstí Svobody", "Hřbitovní",
			"Revoluční", "Sadová", "Jiráskova", "Školní", "Vinohradská", "Komenského", "Mírová", "Křížová",
			"Masarykova", "Třída Svobody", "Karlova", "Dlouhá", "Žižkova", "Purkyňova", "Rybářská", "Sokolská",
			"Rybníček", "Vrchlického", "Štefánikova", "Veselá", "Lipová", "Na Hrázi", "Výšinná", "Nová Cesta", "Česká");
	public static final List<String> CITIES = Collections.unmodifiableList(
			Arrays.asList("Praha", "Brno", "Ostrava", "Plzeň", "Liberec", "Olomouc", "České Budějovice",
					"Hradec Králové", "Ústí nad Labem", "Pardubice", "Zlín", "Karlovy Vary", "Jihlava", "Tábor",
					"Havířov", "Český Těšín", "Mladá Boleslav", "Trutnov", "Přerov", "Kladno", "Opava", "Frýdek-Místek",
					"Děčín", "Karviná", "Jablonec nad Nisou", "Třebíč", "Žďár nad Sázavou", "Kolín", "Uherské Hradiště",
					"Znojmo", "Prostějov", "Litoměřice", "Kroměříž", "Chomutov", "Příbram", "Příbram", "Cheb",
					"Teplice", "Uherský Brod", "Sokolov", "Břeclav", "Litvínov", "Klatovy", "Vsetín", "Nový Jičín"));

	public static final List<String> POST_NUMBER = List.of("10000", "11000", "12000", "13000", "14000", "15000",
			"16000", "17000", "18000", "19000", "20000", "21000", "22000", "23000", "24000", "25000", "26000", "27000",
			"28000", "29000", "30000", "31000", "32000", "33000", "34000", "35000", "36000", "37000", "38000", "39000");

	public static final List<String> COMPANY_NAMES = List.of("Inova", "Sprint", "Polaris", "Forte", "Nexus", "Vertex",
			"Synergie", "Horizon", "Astra", "Axiom", "Optima", "Integra", "Evolve", "Apex", "Dynasty", "Eclipse",
			"Paragon", "Fusion", "Oasis", "Equinox", "Voyage", "Genesis", "Zenith", "Elite", "Harmony", "Stratos",
			"Aurora", "Quantum", "Spectrum");

	public static final List<String> BIRD_NAMES = List.of("Vlaštovka", "Sýkorka", "Krkavec", "Sokol", "Káně",
			"Střízlík", "Sojka", "Datel", "Drozd", "Rorýs", "Husa", "Labuť", "Krahujec", "Sova", "Výr", "Husice",
			"Havran", "Špaček", "Ledňáček", "Sup");

	public static final List<String> NICKNAMES = List.of("CyberSurfer", "PixelPioneer", "SocialSavvy", "DigitalDynamo",
			"ByteBuddy", "InstaGuru", "TikTokTornado", "SnapMaster", "TweetTrendsetter", "ChatChampion", "HashtagHero",
			"EmojiEnthusiast", "StoryStylist", "SelfieStar", "FilterFanatic", "VlogVirtuoso", "Memelord",
			"InfluencerInsider", "StreamSupreme", "GeekyGizmo", "CodeCommander", "JavaJuggernaut", "ByteNinja",
			"SyntaxSamurai", "ClassyCoder", "ObjectOmnipotent", "LoopLegend", "VariableVirtuoso", "DebugDemon",
			"CompilerCrusader", "PixelProdigy", "VirtualVoyager", "AlgorithmAce", "DataDynamo", "ExceptionExpert",
			"BugBuster", "SyntaxSorcerer", "CodeCrusader", "JavaJester");

	public static final List<String> PLAYER_NICKS = List.of("CyberSurfer", "PixelPioneer", "SocialSavvy",
			"DigitalDynamo", "ByteBuddy", "InstaGuru", "TikTokTornado", "SnapMaster", "TweetTrendsetter",
			"ChatChampion", "HashtagHero", "EmojiEnthusiast", "StoryStylist", "SelfieStar", "FilterFanatic",
			"VlogVirtuoso", "Memelord", "InfluencerInsider", "StreamSupreme", "GeekyGizmo", "CodeCommander",
			"JavaJuggernaut", "ByteNinja", "SyntaxSamurai", "ClassyCoder", "ObjectOmnipotent", "LoopLegend",
			"VariableVirtuoso", "DebugDemon", "CompilerCrusader", "PixelProdigy", "VirtualVoyager", "AlgorithmAce",
			"DataDynamo", "ExceptionExpert", "BugBuster", "SyntaxSorcerer", "CodeCrusader", "JavaJester",
			"NerdyNavigator", "CryptoCaptain", "SocialButterfly", "AppArchitect", "WebWizard", "FunctionFreak",
			"PixelArtist", "CyberPhantom", "HackHero", "CacheChampion", "ScreenSage", "WebWeaver", "LogicLover",
			"BitBlazer", "NetworkNomad", "ProtocolPioneer", "BinaryBoss", "StackSultan", "SocialScribe", "RenderRuler",
			"ScriptSorcerer", "HTMLHero", "PixelProwler", "FrameFreak", "DataDreamer", "BotBuilder", "ByteBishop",
			"KeyboardKnight", "DesignDaredevil", "JavaJuggler", "SyntaxStrategist", "TechTactician", "ProgramProdigy",
			"BinaryBard", "PixelPoet", "GigabyteGuru", "TechTrekker", "NetworkNinja", "DataDetective", "MatrixMaster",
			"CodeConductor", "AppAlchemist", "ServerSage", "ClusterChampion", "ScriptSensei", "KeyboardKicker",
			"CacheCrafter", "SocialSpark", "BinaryBeast", "CodeConnoisseur", "BitBrain", "VirtualVanguard",
			"SystemSculptor", "RenderRogue", "CryptoConqueror", "MachineMonarch", "PixelPal", "CompilerCaptain",
			"BitBuilder", "TechTitan", "CloudConqueror", "EchoExplorer", "FunctionFanatic", "RobotRanger");

	public static final List<String> NAME_OF_GAMES = List.of("Call of Duty: Warzone", "Counter-Strike 2", "Battlefield 2042",
			"Overwatch 2", "Team Fortress 2", "Halo Infinite", "Apex Legends", "Rainbow Six Siege", "Valorant",
			"Fortnite", "PUBG: Battlegrounds", "Destiny 2", "Titanfall 2", "Splatoon 3", "Gears of War 5",
			"Left 4 Dead 2", "Dota 2", "League of Legends", "Minecraft (Bedrock/Java - multiplayer)", "Rust",
			"Escape from Tarkov", "DayZ", "Hell Let Loose", "Hunt: Showdown", "Chivalry 2", "Payday 3", "War Thunder",
			"Enlisted", "World of Tanks", "Sea of Thieves");

	public static String randomGameName() {
		return randomElementFrom(NAME_OF_GAMES);
	}

	public static String randomNick() {
		return randomElementFrom(PLAYER_NICKS);
	}

	public static String randomFistName() {
		return randomElementFrom(FIRST_NAMES);
	}

	public static String randomLastName() {
		return randomElementFrom(LAST_NAMES);
	}

	public static final List<String> COURSE_NAMES = List.of("Java 1", "Java 2", "C# 1", "C# 2", "Python", "Databáze I",
			"Databáze II", "Funkcionální programování", "OOP", "PS", "APPS", "SWI");

	public static String generateCompanyName() {
		StringBuilder name = new StringBuilder();
		int numberOfWords = RANDOM.nextInt(2) + 1;
		for (int j = 0; j < numberOfWords; j++) {
			name.append(randomElementFrom(COMPANY_NAMES));
			if (j < numberOfWords - 1) {
				name.append(" ");
			}
		}
		return name.toString();
	}

	public static String generateAddress() {
		return String.format("%s %d, %s, %s", randomElementFrom(STREETS), RANDOM.nextInt(99) + 1,
				randomElementFrom(CITIES), randomElementFrom(POST_NUMBER));
	}

	public static <T> T randomElementFrom(List<T> list) {
		return randomElementFrom(list, RANDOM);
	}

	public static <T> T random(T[] array) {
		return array[RANDOM.nextInt(array.length)];
	}

	public static <T> T randomElementFrom(List<T> list, Random r) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(r.nextInt(list.size()));
	}

	public static LocalDate generatePreviousDate() {
		return LocalDate.of(LocalDate.now().getYear() - RANDOM.nextInt(80), RANDOM.nextInt(12) + 1, 1)
				.plusDays(RANDOM.nextInt(31));
	}

	public static LocalDate generateDateInFuture() {
		return LocalDate.now().plusDays(RANDOM.nextInt(400));
	}

	public static LocalDateTime generatePreviousDateTime() {
		return LocalDateTime.now().minusMinutes(RANDOM.nextInt(100000));
	}

	private Tools() {
		/* hide public one - nothing to do */
	}
}
