package net.kailyard.template.common.domain;

import java.io.Serializable;

/**
 * ajax结果
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 3965689045940101755L;

    public final static int CODE_SUCCESS = 1;
    public final static int C0DE_FAILURE = 0;

    public final static String MSG_SUCCESS = "操作成功!";
    public final static String MSG_FAILURE = "操作失败!";

    public Result(){}

    public Result(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

	/**
	 * 结果状态,1为成功
	 */
	private int code = C0DE_FAILURE;

	/**
	 * 消息
	 */
	private String msg = "";

	/**
	 * 结果对象
	 */
	private Object obj = null;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void setFailure(String msg){
        this.code = C0DE_FAILURE;
        this.msg = msg;
    }

    public void setFailure() {
        setFailure(MSG_FAILURE);
    }

    public void setSuccess(String msg){
        this.code = CODE_SUCCESS;
        this.msg = msg;
    }

    public void setSuccess() {
        setSuccess(MSG_SUCCESS);
    }
}
