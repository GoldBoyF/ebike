/**
 * 弹出消息插件
 */
(function($) {
	$.popMsg = function(options){
		var defaults ={
			cls:"alert-success",
			wrapperAttrs : {
				id: "pop-window-div",
			},
			background:"D9EDF7",
			direction:"top",
			height:60,
			width:600,
			content:"I am showing...",
			flyDelay:500,
			showDelay:500,
			hideDelay:1000,
		};
		if(typeof arguments[0] == "string"){
			if(arguments.length==1){
				options = {content:arguments[0]};
			}else{
				options = {cls:arguments[0],content:arguments[1]};
			}
			
		}
		var settings = $.extend(defaults,options);
		var popWindow = $( "<div />" )
			.attr( settings.wrapperAttrs ).appendTo('body');
		popWindow.html('<p style="text-align:center">'+settings.content+'</p>');
		
		var left = $(document).width()/2 - settings.width/2;		
		var top =  $(document).height() - settings.height-100;
		if(settings.direction == "top"){
			top = 60;
		}
		
		if(settings.cls=="alert-success"){
			popWindow.css({	
				"background-color":"#DFF0D8",
				"border-color":"#D6E9C6",
				"color":"#3C763D",
			});
		}else if(settings.cls=="alert-info"){
			popWindow.css({	
				"background-color":"#D9EDF7",
				"border-color":"#BCE8F1",
				"color":"#31708F",
			});
		}else if(settings.cls=="alert-warn"){
			popWindow.css({	
				"background-color":"#FCF8E3",
				"border-color":"#FAEBCC",
				"color":"#8A6D3B",
			});
		}else if(settings.cls=="alert-error"){
			popWindow.css({	
				"background-color":"#F2DEDE",
				"border-color":"#EBCCD1",
				"color":"#A94442",
			});
		}
		popWindow.css({
			display:"none",
			"border-style":"solid",
			"border-width":"1px",
			"font-weight":"normal",
			"border-radius":"4px",
			position:"absolute",
			
			height:settings.height,
			width:settings.width,
			top:top,
			left:left,
			zIndex:"99999",
		});
		
		
		popWindow.slideDown(settings.flyDelay);
		popWindow.delay(settings.showDelay).fadeOut(settings.hideDelay);
	}
 
})(jQuery);

/*
回到顶部插件(需要easing插件)
 scrollup v2.1.1
 Author: Mark Goodyear - http://markgoodyear.com
 Git: https://github.com/markgoodyear/scrollup

 Copyright 2013 Mark Goodyear.
 Licensed under the MIT license
 http://www.opensource.org/licenses/mit-license.php

 Twitter: @markgdyr

 */
(function($){
	jQuery.easing['jswing'] = jQuery.easing['swing'];
	jQuery.extend( jQuery.easing,
	{
		def: 'easeOutQuad',
		swing: function (x, t, b, c, d) {
			//alert(jQuery.easing.default);
			return jQuery.easing[jQuery.easing.def](x, t, b, c, d);
		},
		easeInQuad: function (x, t, b, c, d) {
			return c*(t/=d)*t + b;
		},
		easeOutQuad: function (x, t, b, c, d) {
			return -c *(t/=d)*(t-2) + b;
		},
		easeInOutQuad: function (x, t, b, c, d) {
			if ((t/=d/2) < 1) return c/2*t*t + b;
			return -c/2 * ((--t)*(t-2) - 1) + b;
		},
		easeInCubic: function (x, t, b, c, d) {
			return c*(t/=d)*t*t + b;
		},
		easeOutCubic: function (x, t, b, c, d) {
			return c*((t=t/d-1)*t*t + 1) + b;
		},
		easeInOutCubic: function (x, t, b, c, d) {
			if ((t/=d/2) < 1) return c/2*t*t*t + b;
			return c/2*((t-=2)*t*t + 2) + b;
		},
		easeInQuart: function (x, t, b, c, d) {
			return c*(t/=d)*t*t*t + b;
		},
		easeOutQuart: function (x, t, b, c, d) {
			return -c * ((t=t/d-1)*t*t*t - 1) + b;
		},
		easeInOutQuart: function (x, t, b, c, d) {
			if ((t/=d/2) < 1) return c/2*t*t*t*t + b;
			return -c/2 * ((t-=2)*t*t*t - 2) + b;
		},
		easeInQuint: function (x, t, b, c, d) {
			return c*(t/=d)*t*t*t*t + b;
		},
		easeOutQuint: function (x, t, b, c, d) {
			return c*((t=t/d-1)*t*t*t*t + 1) + b;
		},
		easeInOutQuint: function (x, t, b, c, d) {
			if ((t/=d/2) < 1) return c/2*t*t*t*t*t + b;
			return c/2*((t-=2)*t*t*t*t + 2) + b;
		},
		easeInSine: function (x, t, b, c, d) {
			return -c * Math.cos(t/d * (Math.PI/2)) + c + b;
		},
		easeOutSine: function (x, t, b, c, d) {
			return c * Math.sin(t/d * (Math.PI/2)) + b;
		},
		easeInOutSine: function (x, t, b, c, d) {
			return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
		},
		easeInExpo: function (x, t, b, c, d) {
			return (t==0) ? b : c * Math.pow(2, 10 * (t/d - 1)) + b;
		},
		easeOutExpo: function (x, t, b, c, d) {
			return (t==d) ? b+c : c * (-Math.pow(2, -10 * t/d) + 1) + b;
		},
		easeInOutExpo: function (x, t, b, c, d) {
			if (t==0) return b;
			if (t==d) return b+c;
			if ((t/=d/2) < 1) return c/2 * Math.pow(2, 10 * (t - 1)) + b;
			return c/2 * (-Math.pow(2, -10 * --t) + 2) + b;
		},
		easeInCirc: function (x, t, b, c, d) {
			return -c * (Math.sqrt(1 - (t/=d)*t) - 1) + b;
		},
		easeOutCirc: function (x, t, b, c, d) {
			return c * Math.sqrt(1 - (t=t/d-1)*t) + b;
		},
		easeInOutCirc: function (x, t, b, c, d) {
			if ((t/=d/2) < 1) return -c/2 * (Math.sqrt(1 - t*t) - 1) + b;
			return c/2 * (Math.sqrt(1 - (t-=2)*t) + 1) + b;
		},
		easeInElastic: function (x, t, b, c, d) {
			var s=1.70158;var p=0;var a=c;
			if (t==0) return b;  if ((t/=d)==1) return b+c;  if (!p) p=d*.3;
			if (a < Math.abs(c)) { a=c; var s=p/4; }
			else var s = p/(2*Math.PI) * Math.asin (c/a);
			return -(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
		},
		easeOutElastic: function (x, t, b, c, d) {
			var s=1.70158;var p=0;var a=c;
			if (t==0) return b;  if ((t/=d)==1) return b+c;  if (!p) p=d*.3;
			if (a < Math.abs(c)) { a=c; var s=p/4; }
			else var s = p/(2*Math.PI) * Math.asin (c/a);
			return a*Math.pow(2,-10*t) * Math.sin( (t*d-s)*(2*Math.PI)/p ) + c + b;
		},
		easeInOutElastic: function (x, t, b, c, d) {
			var s=1.70158;var p=0;var a=c;
			if (t==0) return b;  if ((t/=d/2)==2) return b+c;  if (!p) p=d*(.3*1.5);
			if (a < Math.abs(c)) { a=c; var s=p/4; }
			else var s = p/(2*Math.PI) * Math.asin (c/a);
			if (t < 1) return -.5*(a*Math.pow(2,10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )) + b;
			return a*Math.pow(2,-10*(t-=1)) * Math.sin( (t*d-s)*(2*Math.PI)/p )*.5 + c + b;
		},
		easeInBack: function (x, t, b, c, d, s) {
			if (s == undefined) s = 1.70158;
			return c*(t/=d)*t*((s+1)*t - s) + b;
		},
		easeOutBack: function (x, t, b, c, d, s) {
			if (s == undefined) s = 1.70158;
			return c*((t=t/d-1)*t*((s+1)*t + s) + 1) + b;
		},
		easeInOutBack: function (x, t, b, c, d, s) {
			if (s == undefined) s = 1.70158; 
			if ((t/=d/2) < 1) return c/2*(t*t*(((s*=(1.525))+1)*t - s)) + b;
			return c/2*((t-=2)*t*(((s*=(1.525))+1)*t + s) + 2) + b;
		},
		easeInBounce: function (x, t, b, c, d) {
			return c - jQuery.easing.easeOutBounce (x, d-t, 0, c, d) + b;
		},
		easeOutBounce: function (x, t, b, c, d) {
			if ((t/=d) < (1/2.75)) {
				return c*(7.5625*t*t) + b;
			} else if (t < (2/2.75)) {
				return c*(7.5625*(t-=(1.5/2.75))*t + .75) + b;
			} else if (t < (2.5/2.75)) {
				return c*(7.5625*(t-=(2.25/2.75))*t + .9375) + b;
			} else {
				return c*(7.5625*(t-=(2.625/2.75))*t + .984375) + b;
			}
		},
		easeInOutBounce: function (x, t, b, c, d) {
			if (t < d/2) return jQuery.easing.easeInBounce (x, t*2, 0, c, d) * .5 + b;
			return jQuery.easing.easeOutBounce (x, t*2-d, 0, c, d) * .5 + c*.5 + b;
		}
	});
})(jQuery);

(function($, window, document) {

    // Main function
    $.fn.scrollUp = function (options) {
        // Ensure that only one scrollUp exists
        if ( ! $.data( document.body, 'scrollUp' ) ) {
            $.data( document.body, 'scrollUp', true );
            $.fn.scrollUp.init(options);
        }
    };

    // Init
    $.fn.scrollUp.init = function(options) {
        // Apply any options to the settings, override the defaults
        var o = $.fn.scrollUp.settings = $.extend({}, $.fn.scrollUp.defaults, options),

        // Set scrollTitle
        scrollTitle = (o.scrollTitle) ? o.scrollTitle : o.scrollText,

        // Create element
        $self = $('<a/>', {
            id: o.scrollName,
            href: '#top',
            title: scrollTitle
        }).appendTo('body');

        // If not using an image display text
        if (!o.scrollImg) {
            $self.html(o.scrollText);
        }

        // Minimum CSS to make the magic happen
        $self.css({
            display: 'none',
            position: 'fixed',
            zIndex: o.zIndex
        });

        // Active point overlay
        if (o.activeOverlay) {
            $('<div/>', { id: o.scrollName + '-active' }).css({ position: 'absolute', 'top': o.scrollDistance + 'px', width: '100%', borderTop: '1px dotted' + o.activeOverlay, zIndex: o.zIndex }).appendTo('body');
        }

        // Scroll function
        scrollEvent = $(window).scroll(function() {
            // If from top or bottom
            if (o.scrollFrom === 'top') {
                scrollDis = o.scrollDistance;
            } else {
                scrollDis = $(document).height() - $(window).height() - o.scrollDistance;
            }

            // Switch animation type
            switch (o.animation) {
                case 'fade':
                    $( ($(window).scrollTop() > scrollDis) ? $self.fadeIn(o.animationInSpeed) : $self.fadeOut(o.animationOutSpeed) );
                    break;
                case 'slide':
                    $( ($(window).scrollTop() > scrollDis) ? $self.slideDown(o.animationInSpeed) : $self.slideUp(o.animationOutSpeed) );
                    break;
                default:
                    $( ($(window).scrollTop() > scrollDis) ? $self.show(0) : $self.hide(0) );
            }
        });

        // To the top
        $self.click(function(e) {
            e.preventDefault();
            $('html, body').animate({
                scrollTop:0
            }, o.scrollSpeed, o.easingType);
        });
    };

    // Defaults
    $.fn.scrollUp.defaults = {
        scrollName: 'scrollUp', // Element ID
        scrollDistance: 300, // Distance from top/bottom before showing element (px)
        scrollFrom: 'top', // 'top' or 'bottom'
        scrollSpeed: 300, // Speed back to top (ms)
        easingType: 'linear', // Scroll to top easing (see http://easings.net/)
        animation: 'fade', // Fade, slide, none
        animationInSpeed: 200, // Animation in speed (ms)
        animationOutSpeed: 200, // Animation out speed (ms)
        scrollText: 'Scroll to top', // Text for element, can contain HTML
        scrollTitle: false, // Set a custom <a> title if required. Defaults to scrollText
        scrollImg: false, // Set true to use image
        activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
        zIndex: 2147483647 // Z-Index for the overlay
    };

    // Destroy scrollUp plugin and clean all modifications to the DOM
    $.fn.scrollUp.destroy = function (scrollEvent){
        $.removeData( document.body, 'scrollUp' );
        $( '#' + $.fn.scrollUp.settings.scrollName ).remove();
        $( '#' + $.fn.scrollUp.settings.scrollName + '-active' ).remove();

        // If 1.7 or above use the new .off()
        if ($.fn.jquery.split('.')[1] >= 7) {
            $(window).off( 'scroll', scrollEvent );

        // Else use the old .unbind()
        } else {
            $(window).unbind( 'scroll', scrollEvent );
        }
    };

    $.scrollUp = $.fn.scrollUp;

})(jQuery, window, document);