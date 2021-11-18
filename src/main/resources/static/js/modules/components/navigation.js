import {router} from "../factory.js";

class Menu {
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
        if (e && e.tagName === "A" && e.hasAttribute("value")) {
            this.click(e)
            await router.navigate(parseInt(e.getAttribute("value")));
        }
    }
}

export {Menu};