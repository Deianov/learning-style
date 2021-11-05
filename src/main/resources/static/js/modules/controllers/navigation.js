import router from "../routes/router.js";


class Menu  {
    constructor(element) {
        this.element = element;
        this.element.addEventListener("click", this);
        this.active = undefined;
    }
    click(e) {
        this.setActive(this.active, false)
        this.setActive(e.parentNode, true) // set active to parentNode <li> element
    }
    setActive(e, flag) {
        if (e) {
            e.classList.toggle("active", flag);
        }
        this.active = flag ? e : null
    }
    navigateByIndex(index) {
        this.click(this.element.querySelector(`a[value = "${index}"]`))
    }
    async handleEvent(e) {
        if (e.type === "click") {
           await this.onclick(e.target)
        }
    }
    async onclick(e) {
        if (e && e.tagName === "A") {
            this.click(e)
            if (e.hasAttribute("value")) {
               await router.navigate(parseInt(e.getAttribute("value")));
            }
        }
    }
}

const top = new Menu(document.getElementsByClassName("navbar")[0]);
const navigation = {
    top
}

export default navigation;