package com.ttv.at.test.action;

import java.io.File;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ttv.at.test.error_code;
import com.ttv.at.test.parameter;
import com.ttv.at.test.result;
import com.ttv.at.test.run_type;
import com.ttv.at.test.status_run;
import com.ttv.at.test.testaction;
import com.ttv.at.test.testsetting;

public class action_core extends testaction {
	
	String downloadPath = "D/working/ttv_tool/TTV_Automation/TTVAuto/downloads";

	public action_core(String name, ArrayList<parameter> inputs, ArrayList<parameter> returns, run_type type) {
		super(name, inputs, returns, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public result validate() {
		// execute action
		String act_name = get_name().toLowerCase();
		// if (act_name.equals("openbrowser"))
		// return validate_openbrowser();
		if (act_name.equals("breakloop") || act_name.equals("getnow"))
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		else if (act_name.equals("capturedesktop")) {
			set_capture_image(true);
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
		} else if (act_name.equals("getfloatnumber") || act_name.equals("getlastfloatnumber")
				|| act_name.equals("getnextsiblingfromxpath") || act_name.equals("getparentfromxpath")
				|| act_name.equals("deletefiledownloads")) {
			set_capture_image(true);
			return validate_1_input();
		} else if (act_name.equals("sleep") || act_name.equals("getvaluescount") || act_name.equals("movefirstitem")
				|| act_name.equals("movelastitem") || act_name.equals("movenextitem")
				|| act_name.equals("movepreviousitem") || act_name.equals("moverandomitem")
				|| act_name.equals("checklastitem") || act_name.equals("makeuniqueemail")
				|| act_name.equals("countchars") || act_name.equals("makeuniquestring")) {

			set_capture_image(false);
			return validate_1_input();
		} else if (act_name.equals("substring") || act_name.equals("compare") || act_name.equals("compareignorecase")
				|| act_name.equals("comparesubstring") || act_name.equals("comparesubstringignorecase")
				|| act_name.equals("comparefloat") || act_name.equals("floatgreaterequal")
				|| act_name.equals("subtract") || act_name.equals("multiply") || act_name.equals("executecmd")
				|| act_name.equals("strgreaterequal")) {

			set_capture_image(true);
			return validate_2_inputs();
		} else if (act_name.equals("checkvaluelistcount") || act_name.equals("checkvaluelistcountlessthan")
				|| act_name.equals("checkvaluelistcountmorethan") || act_name.equals("moveitemindex")
				|| act_name.equals("plus") || act_name.equals("strcat") || act_name.equals("strsplit")
				|| act_name.equals("setenv")) {

			set_capture_image(false);
			return validate_2_inputs();
		} else if (act_name.equals("strreplace") || act_name.equals("postmethod") || act_name.equals("putmethod")
				|| act_name.equals("getmethod") || act_name.equals("deletemethod")) {

			set_capture_image(false);
			return validate_3_inputs();
		} else if (act_name.equals("querydbselect") || act_name.equals("querydbupdate")) {

			set_capture_image(false);
			return validate_5_inputs();
		}

		return new result(status_run.STOP, error_code.NOT_SUPPORT,
				"Action '" + get_name() + "' of action_core is not supported");
	}

	result validate_1_input() {
		// Check inputs
		if (get_inputs().size() < 1)
			return new result(status_run.STOP, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' is not provide enough inputs.");
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
	}

	result validate_2_inputs() {
		// Check inputs
		if (get_inputs().size() < 2)
			return new result(status_run.STOP, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' is not provide enough inputs.");
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
	}

	result validate_3_inputs() {
		// Check inputs
		if (get_inputs().size() < 3)
			return new result(status_run.STOP, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' is not provide enough inputs.");
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
	}

	result validate_5_inputs() {
		// Check inputs
		if (get_inputs().size() < 5)
			return new result(status_run.STOP, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' is not provide enough inputs.");
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action OK");
	}

	@Override
	public result execute() {
		return execute(testsetting.get_timeout());
	}

	@Override
	public result execute(int timeout) {
		// execute action
		String act_name = get_name().toLowerCase();
		result run_res = null;
		// if (act_name.equals("openbrowser"))
		// return execute_openbrowser();
		if (act_name.equals("sleep"))
			run_res = execute_sleep();
		else if (act_name.equals("breakloop"))
			run_res = execute_breakloop();
		else if (act_name.equals("movefirstitem"))
			run_res = execute_movefirstitem();
		else if (act_name.equals("movelastitem"))
			run_res = execute_movelastitem();
		else if (act_name.equals("postmethod"))
			run_res = execute_postmethod();
		else if (act_name.equals("putmethod"))
			run_res = execute_putmethod();
		else if (act_name.equals("getmethod"))
			run_res = execute_getmethod();
		else if (act_name.equals("deletemethod"))
			run_res = execute_deletemethod();
		else if (act_name.equals("movenextitem"))
			run_res = execute_movenextitem();
		else if (act_name.equals("movepreviousitem"))
			run_res = execute_movepreviousitem();
		else if (act_name.equals("moverandomitem"))
			run_res = execute_moverandomitem();
		else if (act_name.equals("moveitemindex"))
			run_res = execute_moveitemindex();
		else if (act_name.equals("checklastitem"))
			run_res = execute_checklastitem();
		else if (act_name.equals("checkvaluelistcount"))
			run_res = execute_checkvaluelistcount();
		else if (act_name.equals("checkvaluelistcountlessthan"))
			run_res = execute_checkvaluelistcountlessthan();
		else if (act_name.equals("checkvaluelistcountmorethan"))
			run_res = execute_checkvaluelistcountmorethan();
		else if (act_name.equals("getvaluescount"))
			run_res = execute_getvaluescount();
		else if (act_name.equals("getfloatnumber"))
			run_res = execute_getfloatnumber();
		else if (act_name.equals("getlastfloatnumber"))
			run_res = execute_getlastfloatnumber();
		else if (act_name.equals("comparefloat"))
			run_res = execute_comparefloat();
		else if (act_name.equals("floatgreaterequal"))
			run_res = execute_floatgreaterequal();
		else if (act_name.equals("plus"))
			run_res = execute_plus();
		else if (act_name.equals("subtract"))
			run_res = execute_subtract();
		else if (act_name.equals("multiply"))
			run_res = execute_multiply();
		else if (act_name.equals("compare"))
			run_res = execute_compare();
		else if (act_name.equals("compareignorecase"))
			run_res = execute_compareignorecase();
		else if (act_name.equals("comparesubstringignorecase"))
			run_res = execute_comparesubstringignorecase();
		else if (act_name.equals("comparesubstring"))
			run_res = execute_comparesubstring();
		else if (act_name.equals("countchars"))
			run_res = execute_countchars();
		else if (act_name.equals("makeuniqueemail"))
			run_res = execute_makeuniqueemail();
		else if (act_name.equals("strcat"))
			run_res = execute_strcat();
		else if (act_name.equals("strsplit"))
			run_res = execute_strsplit();
		else if (act_name.equals("strreplace"))
			run_res = execute_strreplace();
		else if (act_name.equals("substring"))
			run_res = execute_substring();
		else if (act_name.equals("makeuniquestring"))
			run_res = execute_makeuniquestring();
		else if (act_name.equals("strgreaterequal"))
			run_res = execute_strgreaterequal();
		else if (act_name.equals("getnextsiblingfromxpath"))
			run_res = execute_getnextsiblingfromxpath();
		else if (act_name.equals("getparentfromxpath"))
			run_res = execute_getparentfromxpath();
		else if (act_name.equals("setenv"))
			run_res = execute_setenv();
		else if (act_name.equals("executecmd"))
			run_res = execute_executecmd();
		else if (act_name.equals("querydbselect"))
			run_res = execute_querydbselect();
		else if (act_name.equals("querydbupdate"))
			run_res = execute_querydbupdate();
		else if (act_name.equals("getnow"))
			run_res = execute_getnow();
		else if (act_name.equals("capturedesktop"))
			run_res = new result(status_run.PASSED, error_code.NO_ERROR, "Capturing Desktop");
		if (run_res == null)
			return new result(status_run.STOP, error_code.NOT_SUPPORT,
					"Action '" + get_name() + "' of action_core is not supported");

		return run_res;
	}

	result execute_postmethod() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			response res = http_common.send_request(get_inputs(), "POST");
			String expected_code = get_inputs().get(5).get_value();
			int status_code = -1;
			if (expected_code.equals("Net::HTTPOK")) {
				status_code = 200;
			}
			if (status_code == res.getStatus()) {
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
			} else {
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '" + get_name()
						+ "' failed, status:" + res.getStatus() + " is different with expected code");
			}

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

	}

	result execute_putmethod() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			response res = http_common.send_request(get_inputs(), "PUT");
			String expected_code = get_inputs().get(5).get_value();
			int status_code = -1;
			if (expected_code.equals("Net::HTTPOK")) {
				status_code = 200;
			}
			if (status_code == res.getStatus()) {
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
			} else {
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '" + get_name()
						+ "' failed, status:" + res.getStatus() + " is different with expected code");
			}
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

	}

	result execute_getmethod() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			response res = http_common.send_request(get_inputs(), "GET");
			String expected_code = get_inputs().get(5).get_value();
			int status_code = -1;
			if (expected_code.equals("Net::HTTPOK")) {
				status_code = 200;
			}
			if (status_code == res.getStatus()) {
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
			} else {
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '" + get_name()
						+ "' failed, status:" + res.getStatus() + " is different with expected code");
			}
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

	}

	result execute_deletemethod() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			response res = http_common.send_request(get_inputs(), "DELETE");
			String expected_code = get_inputs().get(5).get_value();
			int status_code = -1;
			if (expected_code.equals("Net::HTTPOK")) {
				status_code = 200;
			}
			if (status_code == res.getStatus()) {
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
			} else {
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Action '" + get_name()
						+ "' failed, status:" + res.getStatus() + " is different with expected code");
			}
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

	}

	result execute_sleep() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			int sleep_time = Integer.parseInt(get_inputs().get(0).get_value());
			Thread.sleep(sleep_time * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception: " + e);
		}
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
	}

	result execute_getfloatnumber() {
		String inputInt = null;
		String strInput = null;
		if (get_inputs() != null && get_inputs().get(0) != null && get_inputs().get(0).get_value() != null)
			strInput = get_inputs().get(0).get_value().replace(",", "");
		if (strInput != null && strInput.length() > 0) {
			try {
				result check_method = validate();
				if (check_method.get_result() != status_run.PASSED)
					return check_method;
				Pattern intsOnly = Pattern.compile("[0-9\\.\\,]+");
				Matcher makeMatch = intsOnly.matcher(strInput);
				makeMatch.find();
				inputInt = makeMatch.group();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (inputInt == null || inputInt.length() < 1)
				inputInt = "-1";
			else // Remove ',' from the string
				inputInt = inputInt.replace(",", "");
			if (get_Return() != null)
				get_Return().copy_from(inputInt);
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed, number is '"
					+ inputInt + "' , source string is '" + strInput + "'");
		}

		inputInt = "-1";
		if (get_Return() != null)
			get_Return().copy_from(inputInt);
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed, number is '"
				+ inputInt + "' , source string is '" + strInput + "'");
	}

	result execute_getlastfloatnumber() {
		String inputInt = null;
		String strInput = get_inputs().get(0).get_value().replace(",", "");
		if (strInput != null && strInput.length() > 0) {
			try {
				result check_method = validate();
				if (check_method.get_result() != status_run.PASSED)
					return check_method;
				Pattern intsOnly = Pattern.compile("[0-9\\.\\,]+");
				Matcher makeMatch = intsOnly.matcher(strInput);
				for (int i = 0; i < 100; i++)
					if (makeMatch.find())
						inputInt = makeMatch.group();
					else
						break;
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (inputInt == null || inputInt.length() < 1)
				inputInt = "-1";
			else // Remove ',' from the string
				inputInt = inputInt.replace(",", "");
			if (get_Return() != null)
				get_Return().copy_from(inputInt);
			return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed, number is '"
					+ inputInt + "' , source string is '" + strInput + "'");
		}

		inputInt = "-1";
		if (get_Return() != null)
			get_Return().copy_from(inputInt);
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed, number is '"
				+ inputInt + "' , source string is '" + strInput + "'");
	}

	result execute_breakloop() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed.");
	}

	result execute_movefirstitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			for (int i = 0; i < get_inputs().size(); i++)
				if (get_inputs().get(i) != null)
					get_inputs().get(i).move_first();
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_movelastitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			for (int i = 0; i < get_inputs().size(); i++)
				if (get_inputs().get(i) != null)
					get_inputs().get(i).move_last();
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_movenextitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			for (int i = 0; i < get_inputs().size(); i++)
				if (get_inputs().get(i) != null)
					get_inputs().get(i).move_next();
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_movepreviousitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			for (int i = 0; i < get_inputs().size(); i++)
				if (get_inputs().get(i) != null)
					get_inputs().get(i).move_previous();
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_moverandomitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			for (int i = 0; i < get_inputs().size(); i++)
				if (get_inputs().get(i) != null)
					get_inputs().get(i).move_random();
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_moveitemindex() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		ArrayList<String> att_names = new ArrayList<String>();
		ArrayList<String> att_expected_values = new ArrayList<String>();
		int move_index = -1;
		try {
			for (int i = 0; i < get_inputs().size(); i++) {
				if (get_inputs().get(i) != null) {
					String compare_criteria = get_inputs().get(i).get_value();
					if (compare_criteria != null) {
						int separate_point = compare_criteria.indexOf(":");
						if (separate_point > 0 && separate_point < (compare_criteria.length() - 1)) {
							String att_namne = compare_criteria.substring(0, separate_point);
							String att_expected_value = compare_criteria.substring(separate_point + 1,
									compare_criteria.length());
							if (att_namne.toLowerCase().equals("indexno")) {
								move_index = Integer.parseInt(att_expected_value);
								break;
							} else {
								move_index = Integer.parseInt(get_inputs().get(i).get_value());
								break;
							}
						}
					} else
						return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "Compare criteria '"
								+ compare_criteria + "' is not in right format(should be [key:value])'");
				}
			}
			if (move_index > -1) {
				get_inputs().get(0).move_indexno(move_index);
			}
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, current position is '"
							+ get_inputs().get(0).get_array_string_values_current_index()
							+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_checklastitem() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			if (get_inputs().get(0).check_end())
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, current position is '"
								+ get_inputs().get(0).get_array_string_values_current_index()
								+ "' in total number of '' value elements " + get_inputs().get(0).get_array_size());
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, current position is '"
								+ get_inputs().get(0).get_array_string_values_current_index() + "' in total number of '"
								+ get_inputs().get(0).get_array_size() + "' value elements ");

		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_checkvaluelistcount() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			int expected_input_value_count = Integer.parseInt(get_inputs().get(1).get_value());
			int actual_input_value_count = get_inputs().get(0).get_array_size();

			if (get_Return() != null)
				get_Return().copy_from("" + actual_input_value_count);
			if (expected_input_value_count == actual_input_value_count)
				return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name()
						+ "' passed, number of value elements is " + expected_input_value_count);
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, expected number of value elements is "
								+ expected_input_value_count + ", " + "actual number of value elements is "
								+ get_inputs().get(0).get_array_size());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_checkvaluelistcountlessthan() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			int expected_input_value_count = Integer.parseInt(get_inputs().get(1).get_value());
			int actual_input_value_count = get_inputs().get(0).get_array_size();

			if (get_Return() != null)
				get_Return().copy_from("" + actual_input_value_count);
			if (expected_input_value_count > actual_input_value_count)
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, expected number of value elements is "
								+ expected_input_value_count + ", " + "actual number of value elements is "
								+ get_inputs().get(0).get_array_size());
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, expected number of value elements is "
								+ expected_input_value_count + ", " + "actual number of value elements is "
								+ get_inputs().get(0).get_array_size());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_checkvaluelistcountmorethan() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			int expected_input_value_count = Integer.parseInt(get_inputs().get(1).get_value());
			int actual_input_value_count = get_inputs().get(0).get_array_size();

			if (get_Return() != null)
				get_Return().copy_from("" + actual_input_value_count);
			if (expected_input_value_count < actual_input_value_count)
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, expected number of value elements is "
								+ expected_input_value_count + ", " + "actual number of value elements is "
								+ get_inputs().get(0).get_array_size());
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, expected number of value elements is "
								+ expected_input_value_count + ", " + "actual number of value elements is "
								+ get_inputs().get(0).get_array_size());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_getvaluescount() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			int values_count = get_inputs().get(0).get_array_size();

			if (get_Return() != null)
				get_Return().copy_from("" + values_count);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, number of value elements is " + values_count);
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_plus() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		int inputs_size = get_inputs().size();
		boolean half_up = false;
		boolean half_down = false;
		String half_format = "";
		if (inputs_size > 2 && get_inputs().get(inputs_size - 1) != null
				&& get_inputs().get(inputs_size - 1).get_value() != null
				&& get_inputs().get(inputs_size - 1).get_value().length() > 0) {
			String analyzeHelf = get_inputs().get(inputs_size - 1).get_value().toLowerCase();
			if (analyzeHelf.startsWith("half_up:")) {
				half_format = analyzeHelf.substring(8);
				half_up = true;
				inputs_size--;
			} else if (analyzeHelf.startsWith("half_down:")) {
				half_format = analyzeHelf.substring(10);
				half_down = true;
				inputs_size--;
			}
		}
		// Try to calculate total of integer before trying wiht float
		try {
			String value_message = "";
			int total = 0;
			for (int i = 0; i < inputs_size; i++) {
				int value = Integer.parseInt(get_inputs().get(i).get_value());
				if (i == 0)
					value_message = "value0 = '" + value + "'";
				else
					value_message += ", value" + i + " = '" + value + "'";
				total += value;
			}
			if (get_Return() != null)
				get_Return().copy_from("" + total);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, " + value_message + ", total = '" + total + "'");
		} catch (Exception e) {
		}
		try {
			double total = 0;
			String value_message = "";
			for (int i = 0; i < inputs_size; i++) {
				float value = Float.parseFloat(get_inputs().get(i).get_value());
				if (i == 0)
					value_message = "value0 = '" + value + "'";
				else
					value_message += ", value" + i + " = '" + value + "'";
				total += value;
			}
			DecimalFormat df;
			if (half_format != null && half_format.length() > 0)
				df = new DecimalFormat(half_format);
			else
				df = new DecimalFormat("0.00");
			if (half_up)
				df.setRoundingMode(RoundingMode.HALF_UP);
			else if (half_down)
				df.setRoundingMode(RoundingMode.HALF_DOWN);

			String str = df.format(total);
			double result = Double.valueOf(str);

			if (get_Return() != null)
				get_Return().copy_from("" + result);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, " + value_message + ", total = '" + result + "'");
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_substring() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		if (get_inputs().get(0).get_value() == null)
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, first input is null");
		int start_index = 0;
		int len = -1;
		try {
			start_index = Integer.parseInt(get_inputs().get(1).get_value());
			if (get_inputs().size() > 2 && get_inputs().get(2).get_value() != null)
				len = Integer.parseInt(get_inputs().get(2).get_value());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

		String result = "";
		try {
			if (start_index >= 0 && len > 0 && (start_index + len) < get_inputs().get(0).get_value().length())
				result = get_inputs().get(0).get_value().substring(start_index, start_index + len);
			else if (start_index >= 0)
				result = get_inputs().get(0).get_value().substring(start_index);
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}

		if (get_Return() != null)
			get_Return().copy_from(result);

		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, SubString value is '" + result + "'");
	}

	result execute_subtract() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		int inputs_size = get_inputs().size();
		boolean half_up = false;
		boolean half_down = false;
		String half_format = "";
		if (inputs_size > 2 && get_inputs().get(inputs_size - 1) != null
				&& get_inputs().get(inputs_size - 1).get_value() != null
				&& get_inputs().get(inputs_size - 1).get_value().length() > 0) {
			String analyzeHelf = get_inputs().get(inputs_size - 1).get_value().toLowerCase();
			if (analyzeHelf.startsWith("half_up:")) {
				half_format = analyzeHelf.substring(8);
				half_up = true;
				inputs_size--;
			} else if (analyzeHelf.startsWith("half_down:")) {
				half_format = analyzeHelf.substring(10);
				half_down = true;
				inputs_size--;
			}
		}

		try {
			float expected_input_value_count = Float.parseFloat(get_inputs().get(1).get_value());
			float actual_input_value_count = Float.parseFloat(get_inputs().get(0).get_value());
			float total = actual_input_value_count - expected_input_value_count;

			DecimalFormat df;
			if (half_format != null && half_format.length() > 0)
				df = new DecimalFormat(half_format);
			else
				df = new DecimalFormat("0.00");
			if (half_up)
				df.setRoundingMode(RoundingMode.HALF_UP);
			else if (half_down)
				df.setRoundingMode(RoundingMode.HALF_DOWN);

			String str = df.format(total);
			double subtract = Double.valueOf(str);

			if (get_Return() != null)
				get_Return().copy_from("" + subtract);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, total = '" + subtract + "'");
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_multiply() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		int inputs_size = get_inputs().size();
		boolean half_up = false;
		boolean half_down = false;
		String half_format = "";
		if (inputs_size > 2 && get_inputs().get(inputs_size - 1) != null
				&& get_inputs().get(inputs_size - 1).get_value() != null
				&& get_inputs().get(inputs_size - 1).get_value().length() > 0) {
			String analyzeHelf = get_inputs().get(inputs_size - 1).get_value().toLowerCase();
			if (analyzeHelf.startsWith("half_up:")) {
				half_format = analyzeHelf.substring(8);
				half_up = true;
				inputs_size--;
			} else if (analyzeHelf.startsWith("half_down:")) {
				half_format = analyzeHelf.substring(10);
				half_down = true;
				inputs_size--;
			}
		}
		try {
			float expected_input_value_count = Float.parseFloat(get_inputs().get(1).get_value());
			float actual_input_value_count = Float.parseFloat(get_inputs().get(0).get_value());
			float mul = (actual_input_value_count * expected_input_value_count);

			DecimalFormat df;
			if (half_format != null && half_format.length() > 0)
				df = new DecimalFormat(half_format);
			else
				df = new DecimalFormat("0.00");
			if (half_up)
				df.setRoundingMode(RoundingMode.HALF_UP);
			else if (half_down)
				df.setRoundingMode(RoundingMode.HALF_DOWN);

			String str = df.format(mul);

			if (get_Return() != null)
				get_Return().copy_from(str);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, total = '" + str + "'");
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_compare() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		if ((get_inputs().get(0).get_value() == null && get_inputs().get(1).get_value() == null)
				|| (get_inputs().get(0).get_value() != null && get_inputs().get(0).get_value().length() == 0
						&& get_inputs().get(1).get_value() != null && get_inputs().get(1).get_value().length() == 0))
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, both values are blank or null");

		if ((get_inputs().get(0).get_value() == null && get_inputs().get(1).get_value() != null)
				|| (get_inputs().get(0).get_value() != null && get_inputs().get(1).get_value() == null))
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
					"Action '" + get_name() + "' failed, NULL compare to not NULL");

		String str1 = get_inputs().get(0).get_value();
		String str2 = get_inputs().get(1).get_value();

		if (get_inputs().size() > 3)
			try {
				int i_start_index = Integer.parseInt(get_inputs().get(2).get_value());
				int i_length = Integer.parseInt(get_inputs().get(3).get_value());
				if (str1.length() > (i_start_index + i_length))
					str1 = str1.substring(i_start_index, i_start_index + i_length);
				else if (str1.length() > i_start_index && str1.length() <= (i_start_index + i_length))
					str1 = str1.substring(i_start_index, str1.length());
				if (str2.length() > (i_start_index + i_length))
					str2 = str2.substring(i_start_index, i_start_index + i_length);
				else if (str2.length() > i_start_index && str2.length() <= (i_start_index + i_length))
					str2 = str2.substring(i_start_index, str2.length());
			} catch (Exception e) {
			}

		try {
			if (str1.equals(str2))
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, value1 = " + get_inputs().get(0).get_value()
								+ ", value2 = " + get_inputs().get(1).get_value());
			else {
				// Compare regular expression
				if (util.reg_compare(str1, str2))
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Action '" + get_name() + "' passed by regular expression, value1 = "
									+ get_inputs().get(0).get_value() + ", value2 = "
									+ get_inputs().get(1).get_value());
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, value1 = " + get_inputs().get(0).get_value()
								+ ", value2 = " + get_inputs().get(1).get_value());
			}
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_compareignorecase() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			if (get_inputs().get(0).get_value().toLowerCase().equals(get_inputs().get(1).get_value().toLowerCase()))
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, value1 = " + get_inputs().get(0).get_value()
								+ ", value2 = " + get_inputs().get(1).get_value());
			else {
				// Compare ignore case regular expression
				if (util.reg_compare(get_inputs().get(0).get_value().toLowerCase(),
						get_inputs().get(1).get_value().toLowerCase()))
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Action '" + get_name() + "' passed by regular expression, value1 = "
									+ get_inputs().get(0).get_value() + ", value2 = "
									+ get_inputs().get(1).get_value());
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, value1 = " + get_inputs().get(0).get_value()
								+ ", value2 = " + get_inputs().get(1).get_value());
			}
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception : " + e);
		}
	}

	result execute_comparesubstring() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			boolean compared_passed = false;
			String cook_string = get_inputs().get(0).get_value().replace('\n', ' ').replace('\r', ' ').replace('	',
					' ');
			if (get_inputs().get(0).get_value().indexOf(get_inputs().get(1).get_value()) >= 0)
				compared_passed = true;
			else {
				if (cook_string.indexOf(get_inputs().get(1).get_value()) >= 0)
					compared_passed = true;
			}
			if (compared_passed)
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, value1 = " + get_inputs().get(0).get_value()
								+ ", value2 = " + get_inputs().get(1).get_value());
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "Action '" + get_name()
						+ "' failed, value1 = " + cook_string + ", value2 = " + get_inputs().get(1).get_value());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_comparesubstringignorecase() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			boolean compared_passed = false;
			String cook_string = get_inputs().get(0).get_value().replace('\n', ' ').replace('\r', ' ')
					.replace('	', ' ').toLowerCase();
			if (get_inputs().get(0).get_value().toLowerCase()
					.indexOf(get_inputs().get(1).get_value().toLowerCase()) >= 0)
				compared_passed = true;
			else {
				if (cook_string.indexOf(get_inputs().get(1).get_value().toLowerCase()) >= 0)
					compared_passed = true;
			}
			if (compared_passed)
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, value1 = " + get_inputs().get(0).get_value().toLowerCase()
								+ ", value2 = " + get_inputs().get(1).get_value().toLowerCase());
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, value1 = " + cook_string + ", value2 = "
								+ get_inputs().get(1).get_value().toLowerCase());
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value().toLowerCase()
							+ "' , Value2 = '" + get_inputs().get(1).get_value().toLowerCase() + "', exception : " + e);
		}
	}

	result execute_comparefloat() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			float first_number = Float.parseFloat(get_inputs().get(0).get_value());
			float second_number = Float.parseFloat(get_inputs().get(1).get_value());
			float odd_number = 0;
			if (get_inputs().size() > 2)
				odd_number = Float.parseFloat(get_inputs().get(2).get_value());
			if (Math.abs(first_number - second_number) <= odd_number)
				if (odd_number == 0)
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Action '" + get_name() + "' passed, Value1 = '" + get_inputs().get(0).get_value()
									+ "' , Value2 = '" + get_inputs().get(1).get_value() + "'");
				else
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Action '" + get_name() + "' passed, Value1 = '" + get_inputs().get(0).get_value()
									+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', Tolerance = "
									+ odd_number);
			else if (odd_number == 0)
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
								+ "' , Value2 = '" + get_inputs().get(1).get_value() + "'");
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
								+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', Tolerance = " + odd_number);
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_floatgreaterequal() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		try {
			float first_number = Float.parseFloat(get_inputs().get(0).get_value());
			float second_number = Float.parseFloat(get_inputs().get(1).get_value());
			if (first_number >= second_number)
				return new result(status_run.PASSED, error_code.NO_ERROR,
						"Action '" + get_name() + "' passed, Value1 = '" + get_inputs().get(0).get_value()
								+ "' is greater or equal Value2 = '" + get_inputs().get(1).get_value() + "'");
			else
				return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
						"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
								+ "' , Value2 = '" + get_inputs().get(1).get_value() + "'");
		} catch (Exception e) {
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, Value1 = '" + get_inputs().get(0).get_value()
							+ "' , Value2 = '" + get_inputs().get(1).get_value() + "', exception : " + e);
		}
	}

	result execute_getnextsiblingfromxpath() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		String xpath = get_inputs().get(0).get_value();

		if (xpath == null || xpath.length() == 0)
			return new result(status_run.FAILED, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' failed, input is null or blank");

		String s_number = "";
		int start_index = -1, stop_index = 0;
		for (int i = xpath.length() - 1; i >= 0; i--) {
			char c = xpath.charAt(i);
			if (Character.isDigit(c)) {
				s_number = c + s_number;
				if (stop_index == 0)
					stop_index = i;
			} else if (s_number.length() > 0) {
				start_index = i + 1;
				break;
			}
		}

		if (s_number.length() > 0) {
			int i_number = Integer.parseInt(s_number);
			i_number++;
			String res = xpath.substring(0, start_index);
			res = res + i_number;
			res = res + xpath.substring(stop_index + 1);
			if (get_Return() != null)
				get_Return().copy_from(res);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, sibling xpath = " + res + ", original = " + xpath);
		} else {
			if (get_Return() != null)
				get_Return().copy_from(xpath);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, sibling xpath = " + xpath + ", original = " + xpath);
		}
	}

	result execute_getparentfromxpath() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		String xpath = get_inputs().get(0).get_value();

		if (xpath == null || xpath.length() == 0)
			return new result(status_run.FAILED, error_code.REQUIRE_INPUT,
					"Action '" + get_name() + "' failed, input is null or blank");

		int index = xpath.lastIndexOf("/");
		if (index > 0) {
			String res = xpath.substring(0, index);
			if (get_Return() != null)
				get_Return().copy_from(res);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, parent xpath = " + res + ", original = " + xpath);
		} else {
			if (get_Return() != null)
				get_Return().copy_from(xpath);
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Action '" + get_name() + "' passed, parent xpath = " + xpath + ", original = " + xpath);
		}
	}

	result execute_deletefiledownloads() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		File folder = new File(downloadPath);

		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles.length > 0) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					listOfFiles[i].delete();
				}
			}
		}

		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed");
	}
	
	result execute_checkFileExistAfterDownload() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		File folder = new File(downloadPath);
		
		File[] listOfFiles = folder.listFiles();
		
		if (!listOfFiles[0].isFile()) {
			return new result(status_run.FAILED, error_code.ACTION_FAILED,
					"Action '" + get_name() + "' passed");
		}

		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed");
	}

	result execute_makeuniqueemail() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String sEmail = get_inputs().get(0).get_value();

		// Check valid for email
		if (!sEmail.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
					"Action '" + get_name() + "' failed, email: '" + get_inputs().get(0).get_value() + "' not valid.");

		Long iTime = (new Date()).getTime();
		sEmail = sEmail.replaceFirst("@", iTime.toString() + "@");
		if (get_Return() != null)
			get_Return().copy_from(sEmail);
		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, make unique email = " + sEmail);
	}

	result execute_countchars() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String sText = get_inputs().get(0).get_value();
		int iTotalChr = sText.length();

		if (get_Return() != null)
			get_Return().copy_from(Integer.toString(iTotalChr));
		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, total character = " + iTotalChr);
	}

	result execute_strcat() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String res = "";
		for (int i = 0; i < get_inputs().size(); i++) {
			if (get_inputs().get(i) != null) {
				res = res + get_inputs().get(i).get_value();
			} else
				return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "String cat '" + res + "' is null'");
		}
		if (get_Return() != null)
			get_Return().copy_from(res);
		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, string cat = " + res);
	}

	result execute_strsplit() {
		if (get_Return() != null)
			get_Return().clear();
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String mainString = get_inputs().get(0).get_value();
		String split_chars = get_inputs().get(1).get_value();
		String split_description = "";
		if (split_chars == null || split_chars.length() == 0)
			return new result(status_run.FAILED, error_code.REQUIRE_INPUT, "Delimiting character is null/blank");
		if (mainString != null && mainString.length() > 0) {
			String[] splits = mainString.split(split_chars);
			if (splits != null && splits.length > 0)
				for (int i = 0; i < splits.length; i++) {
					if (split_description.length() == 0)
						split_description = "{" + splits[i];
					else if (i == (splits.length - 1))
						split_description = split_description + ", " + splits[i] + "}";
					else
						split_description = split_description + ", " + splits[i];

					if (get_Return() != null)
						get_Return().put_value(splits[i]);
				}

		}
		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, string split result:" + split_description);
	}

	result execute_strreplace() {
		if (get_Return() != null)
			get_Return().clear();
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String mainString = get_inputs().get(0).get_value();
		String old_str = get_inputs().get(1).get_value();
		String new_str = get_inputs().get(2).get_value();
		if (mainString == null || old_str == null || new_str == null)
			return new result(status_run.PASSED, error_code.NO_ERROR, "Some inputs are null");

		String sResult = mainString.replace(old_str, new_str);

		if (get_Return() != null)
			get_Return().copy_from(sResult);

		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, string result:" + sResult);
	}

	result execute_makeuniquestring() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String sString = "";
		Long iTime = (new Date()).getTime();
		if (get_inputs().get(0) != null) {
			sString = get_inputs().get(0).get_value() + "_" + iTime.toString();

			if (get_Return() != null)
				get_Return().copy_from(sString);
		}

		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Action '" + get_name() + "' passed, make unique string = " + sString);
	}

	result execute_strgreaterequal() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String strA = get_inputs().get(0).get_value();
		String strB = get_inputs().get(1).get_value();
		if (strA == strB || (strA == null && strB == null))
			return new result(status_run.PASSED, error_code.NO_ERROR, "Both String are null");
		else if (strA == null || strB == null)
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH, "One of input is null");
		if (strA.compareToIgnoreCase(strB) >= 0)
			return new result(status_run.PASSED, error_code.NO_ERROR,
					"'" + strA + "' is equal or greater than '" + strB + "'");
		else
			return new result(status_run.FAILED, error_code.EXPECTED_NOT_MATCH,
					"'" + strA + "' is less than '" + strB + "'");
	}

	result execute_setenv() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		String strA = get_inputs().get(0).get_value();
		String strB = get_inputs().get(1).get_value();
		if (testsetting.get_common_data() != null && testsetting.get_common_data().get_params() != null) {
			for (parameter scan_param : testsetting.get_common_data().get_params())
				if (scan_param.check_key(strA)) {
					scan_param.copy_from(strB);
					return new result(status_run.PASSED, error_code.NO_ERROR,
							"Parameter '" + strA + "' of testsetting.common.data is reset to value '" + strB + "'");
				}

			testsetting.get_common_data().get_params().add(new parameter(strA, strB));
		}
		return new result(status_run.PASSED, error_code.NO_ERROR,
				"Parameter '" + strA + "' with value '" + strB + "' is add to testsetting.common.data");
	}

	result execute_executecmd() {

		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;

		try {
			// Execute command
			String url = get_inputs().get(0).get_value();
			String file = get_inputs().get(1).get_value();
			String cmd = "cmd /c start cmd.exe /K \"cd ";
			cmd += url;
			cmd += " && ";
			cmd += "ruby ";
			cmd += file;
			Process child = Runtime.getRuntime().exec(cmd);

			// Get output stream to write from it
			OutputStream out = child.getOutputStream();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Action '" + get_name() + "' failed, exception: " + e);
		}
		return new result(status_run.PASSED, error_code.NO_ERROR, "Action '" + get_name() + "' passed");
	}

	result execute_querydbselect() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		ArrayList<parameter> used_inputs = get_inputs();
		String jdbcDriver = used_inputs.get(0).get_value();
		String jdbcUrl = used_inputs.get(1).get_value();
		String jdbcUsername = used_inputs.get(2).get_value();
		String jdbcPassword = used_inputs.get(3).get_value();
		String query = used_inputs.get(4).get_value();

		String Input_Message = "(";
		for (int i = 0; i < used_inputs.size(); i++)
			if (i < (used_inputs.size() - 1))
				Input_Message = Input_Message + used_inputs.get(i).get_value() + ", ";
			else
				Input_Message = Input_Message + used_inputs.get(i).get_value() + ")";

		try {
			if (jdbcDriver != null && jdbcDriver.length() > 0)
				Class.forName(jdbcDriver);
			Connection conn = (Connection) DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
			PreparedStatement query_PreparedStatement = conn.prepareStatement(query);

			/*******************************************/
			// ABLE to Add Parameter for Query
			if (used_inputs.size() > 6) {
				int query_param_count = (used_inputs.size() - 5) / 2;
				for (int i = 0; i < query_param_count; i++) {
					String param_type = used_inputs.get(5 + i * 2).get_value();
					String param_value = used_inputs.get(5 + i * 2 + 1).get_value();
					if (param_type != null && param_value != null) {
						if (param_type.toLowerCase().equals("timestamp"))
							query_PreparedStatement.setTimestamp(i + 1, new java.sql.Timestamp(
									new SimpleDateFormat("MMMM d, yyyy").parse(param_value).getTime()));
						else if (param_type.toLowerCase().equals("int"))
							query_PreparedStatement.setInt(i + 1, Integer.parseInt(param_value));
						else
							query_PreparedStatement.setString(i + 1, param_value);
					}
				}
			}

			ResultSet rs = query_PreparedStatement.executeQuery();

			// Set return
			if (get_returns() != null) {

				for (parameter each_return : get_returns())
					each_return.clear();

				int max_index = get_returns().size();
				ResultSetMetaData rsMetaData;
				rsMetaData = rs.getMetaData();
				if (rsMetaData.getColumnCount() > 0) {
					if (max_index > rsMetaData.getColumnCount())
						max_index = rsMetaData.getColumnCount();
				}

				while (rs.next()) {
					for (int i = 0; i < max_index; i++) {
						get_returns().get(i).put_value(rs.getString(i + 1));
					}
				}
			}

			return new result(status_run.PASSED, error_code.NO_ERROR,
					"Execute Select Query with inputs" + Input_Message + " successful");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Select Query with inputs" + Input_Message + " exception" + e.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Select Query with inputs" + Input_Message + " exception" + e.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Select Query with inputs" + Input_Message + " exception" + e.toString());
		}
	}

	result execute_querydbupdate() {
		result check_method = validate();
		if (check_method.get_result() != status_run.PASSED)
			return check_method;
		ArrayList<parameter> used_inputs = get_inputs();
		String jdbcDriver = used_inputs.get(0).get_value();
		String jdbcUrl = used_inputs.get(1).get_value();
		String jdbcUsername = used_inputs.get(2).get_value();
		String jdbcPassword = used_inputs.get(3).get_value();
		String query = used_inputs.get(4).get_value();

		String Input_Message = "(";
		for (int i = 0; i < used_inputs.size(); i++)
			if (i < (used_inputs.size() - 1))
				Input_Message = Input_Message + used_inputs.get(i).get_value() + ", ";
			else
				Input_Message = Input_Message + used_inputs.get(i).get_value() + ")";

		try {
			if (jdbcDriver != null && jdbcDriver.length() > 0)
				Class.forName(jdbcDriver);
			Connection conn = (Connection) DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
			PreparedStatement query_PreparedStatement = conn.prepareStatement(query);

			/*******************************************/
			// ABLE to Add Parameter for Query
			if (used_inputs.size() > 6) {
				int query_param_count = (used_inputs.size() - 5) / 2;
				for (int i = 0; i < query_param_count; i++) {
					String param_type = used_inputs.get(5 + i * 2).get_value();
					String param_value = used_inputs.get(5 + i * 2 + 1).get_value();
					if (param_type != null && param_value != null) {
						if (param_type.toLowerCase().equals("timestamp"))
							query_PreparedStatement.setTimestamp(i + 1, new java.sql.Timestamp(
									new SimpleDateFormat("MMMM d, yyyy").parse(param_value).getTime()));
						else if (param_type.toLowerCase().equals("int"))
							query_PreparedStatement.setInt(i + 1, Integer.parseInt(param_value));
						else
							query_PreparedStatement.setString(i + 1, param_value);
					}
				}
			}

			Integer row_effect = query_PreparedStatement.executeUpdate();

			// Set return
			if (get_Return() != null)
				get_Return().copy_from(row_effect.toString());

			return new result(status_run.PASSED, error_code.NO_ERROR, "Execute Update Query with inputs" + Input_Message
					+ " successful, number of effect rows is " + row_effect.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Update Query with inputs" + Input_Message + " exception" + e.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Update Query with inputs" + Input_Message + " exception" + e.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new result(status_run.FAILED, error_code.UNKNOWN_ERROR,
					"Execute Update Query with inputs" + Input_Message + " exception" + e.toString());
		}
	}

	result execute_getnow() {
		String return_str = "";
		if (get_inputs() != null && get_inputs().size() > 0 && get_inputs().get(0) != null
				&& get_inputs().get(0).get_value() != null) {
			try {
				SimpleDateFormat headDate = new SimpleDateFormat("yyyy/MM/dd");

				return_str = headDate.format(new Date());

			} catch (Exception e) {
				return new result(status_run.FAILED, error_code.UNKNOWN_ERROR, "Please check the format '"
						+ get_inputs().get(0).get_value() + "' is not in right format time");
			}
		} else
			return_str = (new Date()).toString();
		if (get_Return() != null)
			get_Return().copy_from(return_str);

		return new result(status_run.PASSED, error_code.NO_ERROR, "Current time is " + return_str);
	}
}
