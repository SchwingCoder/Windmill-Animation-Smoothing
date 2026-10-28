package com.schwinglive.windmillsmoothing;

import java.util.Set;
import java.util.function.IntPredicate;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.gameval.AnimationID;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.animsmoothing.AnimationSmoothingPlugin;

@PluginDescriptor(
	name = "Windmill Animation Smoothing",
	description = "Makes windmill animations look smoother.",
	tags = {"animation", "windmill", "immersion", "objects"},
	enabledByDefault = false
)
public class WindmillSmoothingPlugin extends Plugin
{
	private static final Set<Integer> WINDMILL_ANIMATIONS = Set.of(
		AnimationID.MILLSAIL,
		AnimationID.FAI_VARROCK_MILLSAIL,
		AnimationID.BRAIN_MILL_SAIL_ROTATE
	);

	@Inject
	private Client client;

	private IntPredicate originalFilter;
	private IntPredicate windmillFilter;

	@Override
	protected void startUp()
	{
		applyWindmillFilter();
	}

	@Override
	protected void shutDown()
	{
		restoreFilter();
	}

	@Subscribe
	public void onPluginChanged(PluginChanged event)
	{
		if (event.getPlugin().getClass() != AnimationSmoothingPlugin.class)
		{
			return;
		}

		if (event.isLoaded())
		{
			applyWindmillFilter();
		}
		else
		{
			// Animation Smoothing clears the client filter while it shuts down.
			originalFilter = null;
			windmillFilter = null;
		}
	}

	private void applyWindmillFilter()
	{
		IntPredicate currentFilter = client.getAnimationInterpolationFilter();
		if (currentFilter == null || currentFilter == windmillFilter)
		{
			return;
		}

		originalFilter = currentFilter;
		windmillFilter = animationId -> !WINDMILL_ANIMATIONS.contains(animationId)
			&& originalFilter.test(animationId);
		client.setAnimationInterpolationFilter(windmillFilter);
	}

	private void restoreFilter()
	{
		if (windmillFilter != null && client.getAnimationInterpolationFilter() == windmillFilter)
		{
			client.setAnimationInterpolationFilter(originalFilter);
		}

		originalFilter = null;
		windmillFilter = null;
	}
}
