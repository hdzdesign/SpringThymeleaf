// ----------- Inicio Script GTR

$('.timeline-panel').click(function() {
    $('.timeline-body', this).toggle(); // p00f
});

// ----------- FIN script GTR


// ------------- Inicio Script tabla historial
$(document).ready(function () {

	$('.star').on('click', function () {
      $(this).toggleClass('star-checked');
    });

    $('.ckbox label').on('click', function () {
      $(this).parents('tr').toggleClass('selected');
    });

    $('.btn-filter').on('click', function () {
      var $target = $(this).data('target');
      if ($target != 'all') {
        $('.table tr').css('display', 'none');
        $('.table tr[data-status="' + $target + '"]').fadeIn('slow');
      } else {
        $('.table tr').css('display', 'none').fadeIn('slow');
      }
    });

 });

// ---------- FIN Script tabla historial

// templatemo 467 easy profile

// PRELOADER

$(window).load(function(){
    $('.preloader').delay(1000).fadeOut("slow"); // set duration in brackets    
});

// HOME BACKGROUND SLIDESHOW
$(function(){
    jQuery(document).ready(function() {
		$('body').backstretch([
	 		 "images/tm-bg-slide-1.jpg", 
	 		 "images/tm-bg-slide-2.jpg",
			 "images/tm-bg-slide-3.jpg"
	 			], 	{duration: 3200, fade: 1300});
		});
})

var activeEl = 2;
$(function() {
    var items = $('.btn-nav');
    $( items[activeEl] ).addClass('active');
    $( ".btn-nav" ).click(function() {
        $( items[activeEl] ).removeClass('active');
        $( this ).addClass('active');
        activeEl = $( ".btn-nav" ).index( this );
    });
});

function Click_menu(number) {
    ocultartodo();
    if (number == 1) {
    document.getElementById("Pag_ope").style.display = "Block";
    }
    if (number == 2) {
    document.getElementById("Pag_ran").style.display = "Block";
    }
    if (number == 3) {
    document.getElementById("Pag_his").style.display = "Block";
    }
    if (number == 4) {
    document.getElementById("Pag_eve").style.display = "Block";
    }
    if (number == 5) {
    document.getElementById("Pag_gtr").style.display = "Block";
    }
    if (number == 6) {
    document.getElementById("Pag_meg").style.display = "Block";
    }
}

function ocultartodo() {
    document.getElementById("Pag_ope").style.display = "none";
    document.getElementById("Pag_his").style.display = "none";
    document.getElementById("Pag_ran").style.display = "none";
    document.getElementById("Pag_eve").style.display = "none";
    document.getElementById("Pag_gtr").style.display = "none";
    document.getElementById("Pag_meg").style.display = "none";
}


// seccion para megamoneda

// Carousel Auto-Cycle
  $(document).ready(function() {
    $('.carousel').carousel({
      interval: 6000
    })
  });
