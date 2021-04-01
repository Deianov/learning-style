class GoTopButton {
    constructor() {
        this.view;
        this.disabled;
    }
    render() {
        // todo: render
        this.view = document.getElementsByClassName("go-top")[0];
        this.visible(false);
        this.addEvent();
        return this
    }
    visible(flag) {
        if (this.valid) {
            this.view.style.display = flag ? "" : "none";
        }
    }
    addEvent() {
        this.view.addEventListener('click', goTop)
        window.onscroll = this.scrollFunction
    }
    scrollFunction() {
        goTopButton.visible(document.body.scrollTop > 20 || document.documentElement.scrollTop > 20)
    }
    get valid() {
        return !this.disabled && this.view
    }
}

function goTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

/* 
   <button class="go-top">
        <svg viewBox="0 0 16 16" width="16" height="16">
            <title>Go to top</title>
            <g stroke-width="1" stroke="currentColor">
                <polyline fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-miterlimit="10" points="15.5,11.5 8,4 0.5,11.5 "></polyline>
            </g>
        </svg>
    </button>
*/

const goTopButton = new GoTopButton().render();
export default goTopButton;