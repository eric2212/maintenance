/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package eu.kennytv.maintenance.lib.bstats.charts;

import eu.kennytv.maintenance.lib.bstats.charts.CustomChart;
import eu.kennytv.maintenance.lib.bstats.json.JsonObjectBuilder;
import java.util.Map;
import java.util.concurrent.Callable;

public class AdvancedPie
extends CustomChart {
    private final Callable<Map<String, Integer>> callable;

    public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
        Map<String, Integer> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean allSkipped = true;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) continue;
            allSkipped = false;
            valuesBuilder.appendField(entry.getKey(), entry.getValue());
        }
        if (allSkipped) {
            return null;
        }
        return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
    }
}

