package it.eng.test.utilities;

import it.eng.qbe.wizard.EntityClass;
import it.eng.qbe.wizard.GroupByClauseSourceBeanImpl;
import it.eng.qbe.wizard.GroupByFieldSourceBeanImpl;
import it.eng.qbe.wizard.IGroupByClause;
import it.eng.qbe.wizard.IOrderByClause;
import it.eng.qbe.wizard.IOrderGroupByField;
import it.eng.qbe.wizard.ISelectClause;
import it.eng.qbe.wizard.ISelectField;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.qbe.wizard.IWhereClause;
import it.eng.qbe.wizard.IWhereField;
import it.eng.qbe.wizard.OrderByClauseSourceBeanImpl;
import it.eng.qbe.wizard.OrderByFieldSourceBeanImpl;
import it.eng.qbe.wizard.SelectClauseSourceBeanImpl;
import it.eng.qbe.wizard.SelectFieldSourceBeanImpl;
import it.eng.qbe.wizard.SingleDataMartWizardObjectSourceBeanImpl;
import it.eng.qbe.wizard.WhereClauseSourceBeanImpl;
import it.eng.qbe.wizard.WhereFieldSourceBeanImpl;

public class CreateInitialSingleDataMartWizardObject {

	public static ISingleDataMartWizardObject createInitialSingleDataMartWizardObject() {
		
		ISingleDataMartWizardObject query = new SingleDataMartWizardObjectSourceBeanImpl();

		EntityClass ec1 = new EntityClass();

		EntityClass ec2 = new EntityClass();
		
		EntityClass ec3 = new EntityClass();

		ISelectClause selectClause = new SelectClauseSourceBeanImpl();
		
		ISelectField selectField1 = new SelectFieldSourceBeanImpl();

		ISelectField selectField2 = new SelectFieldSourceBeanImpl();

		ISelectField selectField3 = new SelectFieldSourceBeanImpl();

		IWhereClause whereClause = new WhereClauseSourceBeanImpl();

		IWhereField whereField1 = new WhereFieldSourceBeanImpl();

		IWhereField whereField2 = new WhereFieldSourceBeanImpl();

		IWhereField whereField3 = new WhereFieldSourceBeanImpl();

		IOrderByClause orderByClause = new OrderByClauseSourceBeanImpl();

		IOrderGroupByField orderByField1 = new OrderByFieldSourceBeanImpl();

		IOrderGroupByField orderByField2 = new OrderByFieldSourceBeanImpl();

		IOrderGroupByField orderByField3 = new OrderByFieldSourceBeanImpl();
		
		IGroupByClause groupByClause = new GroupByClauseSourceBeanImpl();
		 
		IOrderGroupByField groupByField1 = new GroupByFieldSourceBeanImpl();

		IOrderGroupByField groupByField2 = new GroupByFieldSourceBeanImpl();

		IOrderGroupByField groupByField3 = new GroupByFieldSourceBeanImpl();

		// ENTITY CLASS

		ec1.setClassName("EntityClass1");
		ec1.setClassAlias("EntityClass1Alias");
		
		ec2.setClassName("EntityClass2");
		ec2.setClassAlias("EntityClass2Alias");

		ec3.setClassName("EntityClass3");
		ec3.setClassAlias("EntityClass3Alias");
		
		query.addEntityClass(ec1);
		query.addEntityClass(ec2);
		query.addEntityClass(ec3);
		
		// SELECT

		selectField1.setId("1");
		selectField1.setFieldName("Select1");
		selectField1.setFieldAlias("Select1Alias");
		selectField1.setFieldEntityClass(ec1);

		selectField2.setId("2");
		selectField2.setFieldName("Select2");
		selectField2.setFieldAlias("Select2Alias");
		selectField2.setFieldEntityClass(ec2);

		selectField3.setId("3");
		selectField3.setFieldName("Select3");
		selectField3.setFieldAlias("Select3Alias");
		selectField3.setFieldEntityClass(ec3);

		selectClause.addSelectField(selectField1);
		selectClause.addSelectField(selectField2);
		selectClause.addSelectField(selectField3);

		query.setSelectClause(selectClause);

		// WHERE

		whereField1.setId("1");
		whereField1.setFieldName("Where1");
		whereField1.setFieldValue("Where1Value");
		whereField1.setFieldEntityClassForLeftCondition(ec1);
		whereField1.setFieldEntityClassForRightCondition(ec1);
		whereField1.setFieldOperator("=");
		whereField1.setNextBooleanOperator("AND");

		whereField2.setId("2");
		whereField2.setFieldName("Where2");
		whereField2.setFieldValue("Where2Value");
		whereField2.setFieldEntityClassForLeftCondition(ec2);
		whereField2.setFieldEntityClassForRightCondition(ec2);
		whereField2.setFieldOperator("=");
		whereField2.setNextBooleanOperator("AND");

		whereField3.setId("3");
		whereField3.setFieldName("Where3");
		whereField3.setFieldValue("Where3Value");
		whereField3.setFieldEntityClassForLeftCondition(ec3);
		whereField3.setFieldEntityClassForRightCondition(ec3);
		whereField3.setFieldOperator("=");
		whereField3.setNextBooleanOperator("AND");
		
		whereClause.addWhereField(whereField1);
		whereClause.addWhereField(whereField2);
		whereClause.addWhereField(whereField3);

		query.setWhereClause(whereClause);

		// ORDER BY

		orderByField1.setId("1");
		orderByField1.setFieldName("OrderBy1");

		orderByField2.setId("2");
		orderByField2.setFieldName("OrderBy2");
		
		orderByField3.setId("3");
		orderByField3.setFieldName("OrderBy3");
		
		orderByClause.addOrderByField(orderByField1);
		orderByClause.addOrderByField(orderByField2);
		orderByClause.addOrderByField(orderByField3);

		query.setOrderByClause(orderByClause);

		// GROUP BY
		
		groupByField1.setId("1");
		groupByField1.setFieldName("GroupBy1");
		
		groupByField2.setId("2");
		groupByField2.setFieldName("GroupBy2");
		
		groupByField3.setId("3");
		groupByField3.setFieldName("GroupBy3");
		
		groupByClause.addGroupByField(groupByField1);
		groupByClause.addGroupByField(groupByField2);
		groupByClause.addGroupByField(groupByField3);

		query.setGroupByClause(groupByClause);

		return query;
		
	}

}

