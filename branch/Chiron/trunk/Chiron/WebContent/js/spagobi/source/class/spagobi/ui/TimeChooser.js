/* ************************************************************************

   qooxdoo - the new era of web development

   http://qooxdoo.org

   Copyright:
     2006 STZ-IDA, Germany, http://www.stz-ida.de

   License:
     LGPL: http://www.gnu.org/licenses/lgpl.html
     EPL: http://www.eclipse.org/org/documents/epl-v10.php
     See the LICENSE file in the project's top-level directory for details.

   Authors:
     * Til Schneider (til132)
     * Martin Wittemann (martinwittemann)

 ************************************************************************ 
*/

/**
 * A *date chooser* is a small calendar including a navigation bar to switch the shown
 * month. It includes a column for the calendar week and shows one month. Selecting
 * a date is as easy as clicking on it.
 *
 * To be conform with all form widgets, the {@link qx.ui.form.IFormElement} interface
 * is implemented.
 *
 * The following example creates and adds a date chooser to the root element.
 * A listener alerts the user if a new date is selected.
 *
 * <pre>
 * var chooser = new qx.ui.control.DateChooser();
 * this.getRoot().add(chooser, { left : 20, top: 20});
 *
 * chooser.addListener("changeValue", function(e) {
 *   alert(e.getData());
 * });
 * </pre>
 *
 * Additionally to a selection event a execute event is available which is
 * fired by doubleclick or taping the space / enter key. With this event you
 * can for example save the selection and close the date chooser.
 */
qx.Class.define("spagobi.ui.TimeChooser",
{
  extend : qx.ui.core.Widget,
  include : qx.ui.core.MExecutable,
  implement : qx.ui.form.IFormElement,


  /*
  *****************************************************************************
     CONSTRUCTOR
  *****************************************************************************
  */

  /**
   * @param date {Date ? null} The initial date to show. If <code>null</code>
   * the current day (today) is shown.
   */
  construct : function(date)
  {
    this.base(arguments);
    
    //INITIALIZATION
	var d = new Date();//today's date
	this.fullTime = {};
	this.fullTime.hour = d.getHours();
	this.fullTime.minute = d.getMinutes();
	//this._time = new qx.util.format.DateFormat();
	
    // set the layout
    var layout = new qx.ui.layout.VBox();
    this._setLayout(layout);

    // create the child controls
    
    this._createChildControl("date-pane");

    // Support for key events
    //this.addListener("keypress", this._onKeyPress);

   	this._updateDatePane();
    
    // listen for locale changes
    if (qx.core.Variant.isSet("qx.dynamicLocaleSwitch", "on")) {
      qx.locale.Manager.getInstance().addListener("changeLocale", this._updateDatePane, this);
    }

    // register mouse up and down handler
    this.addListener("mousedown", this._onMouseUpDown, this);
    this.addListener("mouseup", this._onMouseUpDown, this);
  },


  /*
  *****************************************************************************
     EVENTS
  *****************************************************************************
  */

  events :
  {
    /** Fired when the value was modified */
    changeValue : "qx.event.type.Data"
  },




  /*
  *****************************************************************************
     PROPERTIES
  *****************************************************************************
  */

  properties :
  {
    // overridden
    appearance :
    {
      refine : true,
      init   : "datechooser"
    },

    // overrridden
    width :
    {
      refine : true,
      init : 200
    },

    // overridden
    height :
    {
      refine : true,
      init : 150
    },

    /** The currently shown month. 0 = january, 1 = february, and so on. */
    shownMonth :
    {
      check : "Integer",
      init : null,
      nullable : true,
      event : "changeShownMonth"
    },

    /** The currently shown year. */
    shownYear :
    {
      check : "Integer",
      init : null,
      nullable : true,
      event : "changeShownYear"
    },

    /** The currently selected date. */
    date :
    {
      check : "Date",
      init : null,
      nullable : true,
      apply : "_applyDate",
      event : "changeDate"
    },

    /** The name of the widget. Mainly used for serialization proposes. */
    name :
    {
      check : "String",
      nullable : true,
      event : "changeName"
    }
  },




  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    __weekdayLabelArr : null,
    __dayLabelArr : null,
	__dayLabelArr2 : null,
	__AmPmArr	: null,
	fullTime	: null,
	_time	:	null,

    /*
    ---------------------------------------------------------------------------
      FORM API IMPLEMENTATION
    ---------------------------------------------------------------------------
    */

    /**
     * Sets the element's string value. The String should by excepted by the
     * JavaScript Date-Object.
     *
     * @param value {String} The new date value as a JavaScript confrom date string.
     * @return {String} the value
     */
    setValue : function(value)
    {
      if (qx.core.Variant.isSet("qx.debug", "on")) {
        this.assertType(value, "string");
      }

      this.setDate(new Date(value));
      return value;
    },


    /**
     * The element's user set value (date) as a String.
     *
     * @return {String} The current set date.
     */
    getValue : function() {
      return this.getDate().toString();
    },




    /*
    ---------------------------------------------------------------------------
      WIDGET INTERNALS
    ---------------------------------------------------------------------------
    */

    // overridden
    _createChildControlImpl : function(id)
    {
      var control;

      switch(id)
      {
        

        // DATE PANE STUFF
        case "date-pane":
          var controlLayout = new qx.ui.layout.Grid()
          control = new qx.ui.container.Composite(controlLayout);

          for (var i = 0; i < 4; i++) {
            // controlLayout.setColumnWidth(i, 24);
            controlLayout.setColumnFlex(i, 1);
          }

          for (var i = 0; i < 7; i++) {
            controlLayout.setRowFlex(i, 1);
            // controlLayout.setRowHeight(i, 18);
          }

          this.__weekdayLabelArr = [];

          for (var i=0; i<4; i+=2)
          {
            var label = new qx.ui.basic.Label();
            label.setAllowGrowX(true);
            label.setAllowGrowY(true);
            label.setAppearance("datechooser-weekday");
            label.setSelectable(false);
            label.setAnonymous(true);
            label.setCursor("default");

            control.add(label, {column: i, row: 0, colSpan: 2});
            this.__weekdayLabelArr.push(label);
          }

          // Add the days
          this.__dayLabelArr = [];
          this.__dayLabelArr2 = [];
		  this.__AmPmArr = [];
		  	
          for (var y = 0; y < 6; y++)
          {
            // Add the day labels
            for (var x = 0; x < 2; x++)
            {
              var label = new qx.ui.basic.Label();
              label.setAllowGrowX(true);
              label.setAllowGrowY(true);
              label.setAppearance("datechooser-day");
              label.setCursor("default");

              label.addListener("mousedown", this._onDayClicked, this);
              label.addListener("dblclick", this._onDayDblClicked, this);
              control.add(label, {column:x , row:y + 1});
              this.__dayLabelArr.push(label);
            }
            
            for (var x = 2; x < 4; x++)
            {
              var label = new qx.ui.basic.Label();
              label.setAllowGrowX(true);
              label.setAllowGrowY(true);
              label.setAppearance("datechooser-minutes");
              label.setCursor("default");

              label.addListener("mousedown", this._onDayClicked, this);
              label.addListener("dblclick", this._onDayDblClicked, this);
              control.add(label, {column:x , row:y + 1});
              this.__dayLabelArr2.push(label);
            }
          }
			
		  for (var x = 0; x < 3; x++)
            {
              var label = new qx.ui.basic.Label();
              label.setAllowGrowX(true);
              label.setAllowGrowY(true);
              label.setAppearance("datechooser-day");
              label.setCursor("default");

              label.addListener("mousedown", this._onTimeofDayClicked, this);
              control.add(label, {column:x , row:7});
              this.__AmPmArr.push(label);
            }  	
          this._add(control);
          break;
      }

      return control || this.base(arguments, id);
    },


    // apply methods
    _applyDate : function(value, old)
    {
      /*	
      // fire the changeValue event
      this.fireDataEvent("changeValue", value == null ? "" : value.toString());
	  
      if ((value != null) && (this.getShownMonth() != value.getMonth() || this.getShownYear() != value.getFullYear()))
      {
        // The new date is in another month -> Show that month
        this.showMonth(value.getMonth(), value.getFullYear());
      }
      */
    },



    /*
    ---------------------------------------------------------------------------
      EVENT HANDLER
    ---------------------------------------------------------------------------
    */

    /**
     * Hendler which stops the propagation of the click event if
     * the Navigation bar or kalender headers will be clicked.
     *
     * @param e {qx.event.type.Mouse} The mouse up / down event
     */
    _onMouseUpDown : function(e) {
      var target = e.getTarget();

      if (target == this._getChildControl("date-pane")) {
        e.stopPropagation();
        return;
      }
    },


    /**
     * Event handler. Called when a day has been clicked.
     *
     * @param evt {qx.event.type.Data} The event.
     */
    _onDayClicked : function(evt)
    {
      var time = evt.getCurrentTarget().dateTime;
      
      if(time.hour == undefined){
      	this.fullTime.minute = time.minute;
      	
      }
      else if(time.minute == undefined){
      	this.fullTime.hour = time.hour;
      	
      } 
      
      this.setDate(new Date(70,1,1,this.fullTime.hour,this.fullTime.minute,0)); 
      //this.setDate(new Date(70,1,1,9,25,0));
      
    },

	_onTimeofDayClicked: function(e){
		
		//alert(this._time);
		var currentAmPm = this._time.substring(6,8);
		var selectedAmPM = e.getTarget().getContent();
		
		if (currentAmPm != selectedAmPM){
			if(selectedAmPM == 'AM'){
				this.fullTime.hour-=12;
			}
			else if(selectedAmPM == 'PM'){
				this.fullTime.hour+=12;
			}
			this.setDate(new Date(70,1,1,this.fullTime.hour,this.fullTime.minute,0)); 
		}	
	},

    /**
     * Event handler. Called when a day has been double-clicked.
     */
    _onDayDblClicked : function() {
      this.execute();
    },


    /**
     * Event handler. Called when a key was pressed.
     *
     * @param evt {qx.event.type.Data} The event.
     */
    /* 
    _onKeyPress : function(evt)
    {
      var dayIncrement = null;
      var monthIncrement = null;
      var yearIncrement = null;

      if (evt.getModifiers() == 0)
      {
        switch(evt.getKeyIdentifier())
        {
          case "Left":
            dayIncrement = -1;
            break;

          case "Right":
            dayIncrement = 1;
            break;

          case "Up":
            dayIncrement = -7;
            break;

          case "Down":
            dayIncrement = 7;
            break;

          case "PageUp":
            monthIncrement = -1;
            break;

          case "PageDown":
            monthIncrement = 1;
            break;

          case "Escape":
            if (this.getDate() != null)
            {
              this.setDate(null);
              return true;
            }

            break;

          case "Enter":
          case "Space":
            if (this.getDate() != null) {
              this.execute();
            }

            return;
        }
      }
      else if (evt.isShiftPressed())
      {
        switch(evt.getKeyIdentifier())
        {
          case "PageUp":
            yearIncrement = -1;
            break;

          case "PageDown":
            yearIncrement = 1;
            break;
        }
      }

      if (dayIncrement != null || monthIncrement != null || yearIncrement != null)
      {
        var date = this.getDate();

        if (date != null) {
          date = new Date(date.getTime()); // TODO: Do cloning in getter
        }

        if (date == null) {
          date = new Date();
        }
        else
        {
          if (dayIncrement != null){date.setDate(date.getDate() + dayIncrement);}
          if (monthIncrement != null){date.setMonth(date.getMonth() + monthIncrement);}
          if (yearIncrement != null){date.setFullYear(date.getFullYear() + yearIncrement);}
        }

        //this.setDate(date);
      }
    },
	*/

    


    /**
     * Event handler. Used to handle the key events.
     *
     * @param e {qx.event.type.Data} The event.
     */
     /*
    handleKeyPress : function(e) {
      this._onKeyPress(e);
    },
	*/

    /**
     * Updates the date pane.
     */
    _updateDatePane : function()
    {
      //var DateChooser = qx.ui.control.DateChooser;
	  /*
      var today = new Date();
      var todayYear = today.getFullYear();
      var todayMonth = today.getMonth();
      var todayDayOfMonth = today.getDate();

      var selDate = this.getDate();
      var selYear = (selDate == null) ? -1 : selDate.getFullYear();
      var selMonth = (selDate == null) ? -1 : selDate.getMonth();
      var selDayOfMonth = (selDate == null) ? -1 : selDate.getDate();

      var shownMonth = this.getShownMonth();
      var shownYear = this.getShownYear();
	  */
      
      
      for (var i=0; i<2; i++)
      {
        var dayLabel = this.__weekdayLabelArr[i];
        
        if(i==0){
        	dayLabel.setContent("Hours");
        }	
        else{
        	dayLabel.setContent("Minutes");
        }
        
      }

      
      for (var week=0; week<6; week++)
      {
        
        for (var i=0; i<2; i++)
        {
          var dayLabel = this.__dayLabelArr[week * 2 + i];
		
          dayLabel.setContent("" + (week * 2 + i+1));
          dayLabel.dateTime = {};
          dayLabel.dateTime.hour = week * 2 + i+1;//dayLabel.getContent()

          // Go to the next day
          //helpDate.setDate(helpDate.getDate() + 1);
        }
        for (var i=0; i<2; i++)
        {
          var dayLabel = this.__dayLabelArr2[week * 2 + i];

          dayLabel.setContent("" + (week * 10 + i*5));
          dayLabel.dateTime = {};
          dayLabel.dateTime.minute = week * 10 + i*5;

          // Go to the next day
          //helpDate.setDate(helpDate.getDate() + 1);
        }
      }
	
	  var timeOfDay = ["AM","PM","Any"];
      for(var b=0; b<3; b++){
      	var button = this.__AmPmArr[b];
      	button.setContent(timeOfDay[b]);
      }
    },
    
    //Function to highlight the selected time in pop-up list
	setTime: function(t){
		//alert(t);
		this._time = t;
		
		
	},
  },



  /*
  *****************************************************************************
     DESTRUCTOR
  *****************************************************************************
  */

  destruct : function()
  {
    if (qx.core.Variant.isSet("qx.dynamicLocaleSwitch", "on")) {
      qx.locale.Manager.getInstance().removeListener("changeLocale", this._updateDatePane, this);
    }

    this._disposeArray("__weekdayLabelArr", 1);
    this._disposeArray("__dayLabelArr", 1);
    this._disposeArray("__dayLabelArr2", 1);
    this._disposeArray("__AmPmArr", 1);
  }
});
