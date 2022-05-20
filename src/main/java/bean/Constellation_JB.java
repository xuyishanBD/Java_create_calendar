package bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
//星座配置类
public class Constellation_JB {
    private String name;
    private String start;
    private String end;
}
