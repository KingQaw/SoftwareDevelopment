package com.zhao.travelguide.pojo.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataOverviewVO {
    private Integer pendingGuidesCounts;
    private Integer approvedGuidesCounts;
    private Integer rejectedGuidesCounts;
    private ChartData sevenDayData;
    private ChartData monthData;
    private ChartData yearData;

    private VisitData visitSevenData;
    private VisitData visitMonthData;

    private List<RegionCount> regionData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private List<String> date;
        private List<Integer> userCounts;
        private List<Integer> guideCounts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitData {
        private List<String> date;
        private List<Integer> visitCounts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegionData {
        private List<String> name;
        private List<Integer> value;
    }
}
