package frodez.dao.result.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 可用任务信息
 * @author Frodez
 * @date 2019-03-21
 */
@Data
@NoArgsConstructor
@ApiModel(value = "可用任务信息返回数据")
public class AvailableTaskInfo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;

	/**
	 * 可被强制中断(只是建议,非必要)
	 */
	@ApiModelProperty(value = "可被强制中断(只是建议,非必要)")
	private Boolean permitForceInterrupt;

}
