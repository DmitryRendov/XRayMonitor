package me.drendov.XRayMonitor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@DisplayName("Listeners onOreBreak Tests")
class ListenersTest {

    @Mock
    private XRayMonitor mockPlugin;

    @Mock
    private Server mockServer;

    @Mock
    private org.bukkit.configuration.file.FileConfiguration mockConfig;

    private Listeners listeners;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock the singleton
        try (MockedStatic<XRayMonitor> mockedXray = mockStatic(XRayMonitor.class)) {
            mockedXray.when(XRayMonitor::getInstance).thenReturn(mockPlugin);
            listeners = new Listeners();
        }
        
        // Set up plugin instance
        when(mockPlugin.getServer()).thenReturn(mockServer);
        when(mockPlugin.getConfig()).thenReturn(mockConfig);
    }

    private BlockBreakEvent createBlockBreakEvent(String playerName, Material blockType) {
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getName()).thenReturn(playerName);

        Block mockBlock = mock(Block.class);
        when(mockBlock.getType()).thenReturn(blockType);

        BlockBreakEvent event = mock(BlockBreakEvent.class);
        when(event.getPlayer()).thenReturn(mockPlayer);
        when(event.getBlock()).thenReturn(mockBlock);

        return event;
    }

    private Player createStaffPlayer(boolean hasPermission) {
        Player staffPlayer = mock(Player.class);
        when(staffPlayer.hasPermission("xcheck.receive")).thenReturn(hasPermission);
        return staffPlayer;
    }

    @SuppressWarnings("unchecked")
    private void mockOnlinePlayers(Collection<? extends Player> players) {
        doReturn(players).when(mockServer).getOnlinePlayers();
    }

    @Test
    @DisplayName("Should return early for non-ore blocks")
    void testNonOreBlockReturnsEarly() {
        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DIRT);

        listeners.onOreBreak(event);

        // Verify no interaction with server
        verify(mockServer, never()).getOnlinePlayers();
    }

    @Test
    @DisplayName("Should notify staff when iron ore is broken and config enabled")
    void testIronOreBrokenWithConfigEnabled() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.iron")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.IRON_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("iron ore"));
    }

    @Test
    @DisplayName("Should not notify staff when iron ore broken with config disabled")
    void testIronOreBrokenWithConfigDisabled() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.iron")).thenReturn(false);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.IRON_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer, never()).sendMessage(any(String.class));
    }

    @Test
    @DisplayName("Should notify staff when deepslate iron ore is broken")
    void testDeepslateIronOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.iron")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_IRON_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("iron ore"));
    }

    @Test
    @DisplayName("Should notify staff when copper ore is broken")
    void testCopperOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.copper")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.COPPER_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("copper ore"));
    }

    @Test
    @DisplayName("Should notify staff when coal ore is broken")
    void testCoalOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.coal")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.COAL_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("coal ore"));
    }

    @Test
    @DisplayName("Should notify staff when redstone ore is broken")
    void testRedstoneOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.redstone")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.REDSTONE_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("redstone ore"));
    }

    @Test
    @DisplayName("Should notify staff when gold ore is broken")
    void testGoldOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.gold")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.GOLD_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("gold ore"));
    }

    @Test
    @DisplayName("Should notify staff when nether gold ore is broken")
    void testNetherGoldOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.gold")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.NETHER_GOLD_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("gold ore"));
    }

    @Test
    @DisplayName("Should notify staff when lapis ore is broken")
    void testLapisOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.lapis")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.LAPIS_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("lapis ore"));
    }

    @Test
    @DisplayName("Should notify staff when emerald ore is broken")
    void testEmeraldOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.emerald")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.EMERALD_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("emerald ore"));
    }

    @Test
    @DisplayName("Should notify staff when diamond ore is broken")
    void testDiamondOreBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.diamond")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DIAMOND_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("diamond ore"));
    }

    @Test
    @DisplayName("Should notify staff when ancient debris is broken")
    void testAncientDebrisBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.ancient")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.ANCIENT_DEBRIS);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("ancient debris"));
    }

    @Test
    @DisplayName("Should notify staff when spawner is broken")
    void testSpawnerBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.spawner")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.SPAWNER);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("spawner"));
    }

    @Test
    @DisplayName("Should notify staff when mossy cobblestone is broken")
    void testMossyCobbleStoneBroken() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.mossy")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.MOSSY_COBBLESTONE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("mossy cobblestone"));
    }

    @Test
    @DisplayName("Should not notify players without xcheck.receive permission")
    void testNoNotificationWithoutPermission() {
        Player unprivilegedPlayer = createStaffPlayer(false);
        Collection<Player> players = new ArrayList<>();
        players.add(unprivilegedPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.iron")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.IRON_ORE);
        listeners.onOreBreak(event);

        verify(unprivilegedPlayer, never()).sendMessage(any(String.class));
    }

    @Test
    @DisplayName("Should notify multiple staff members")
    void testNotifyMultipleStaffMembers() {
        Player staff1 = createStaffPlayer(true);
        Player staff2 = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staff1);
        players.add(staff2);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.diamond")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DIAMOND_ORE);
        listeners.onOreBreak(event);

        verify(staff1).sendMessage(contains("diamond ore"));
        verify(staff2).sendMessage(contains("diamond ore"));
    }

    @Test
    @DisplayName("Should notify deepslate coal ore")
    void testDeepslateCoal() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.coal")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_COAL_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("coal ore"));
    }

    @Test
    @DisplayName("Should notify deepslate diamond ore")
    void testDeepslateDiamond() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.diamond")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_DIAMOND_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("diamond ore"));
    }

    @Test
    @DisplayName("Should notify deepslate lapis ore")
    void testDeepslateLapis() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.lapis")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_LAPIS_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("lapis ore"));
    }

    @Test
    @DisplayName("Should notify deepslate copper ore")
    void testDeepslateCopper() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.copper")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_COPPER_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("copper ore"));
    }

    @Test
    @DisplayName("Should notify deepslate redstone ore")
    void testDeepslateRedstone() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.redstone")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_REDSTONE_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("redstone ore"));
    }

    @Test
    @DisplayName("Should notify deepslate gold ore")
    void testDeepslateGold() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.gold")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_GOLD_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("gold ore"));
    }

    @Test
    @DisplayName("Should notify deepslate emerald ore")
    void testDeepslateEmerald() {
        Player staffPlayer = createStaffPlayer(true);
        Collection<Player> players = new ArrayList<>();
        players.add(staffPlayer);
        
        mockOnlinePlayers(players);
        when(mockConfig.getBoolean("logOreBreaks.emerald")).thenReturn(true);

        BlockBreakEvent event = createBlockBreakEvent("TestPlayer", Material.DEEPSLATE_EMERALD_ORE);
        listeners.onOreBreak(event);

        verify(staffPlayer).sendMessage(contains("emerald ore"));
    }
}
