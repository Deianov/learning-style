import router from "../pages/router.js";


class Menu  {
    constructor(element) {
        this.element = element;
        this.element.addEventListener('click', this);
        this.active;
    }
    click(e) {
        this.setActive(this.active, false)
        this.setActive(e, true)
    }
    setActive(e, flag) {
        if (e) {
            e.classList.toggle('active', flag);
        }
        this.active = flag ? e : null
    }
    navigateByIndex(index) {
        this.click(this.element.querySelector(`a[value = '${index}']`))
    }
    handleEvent(e) {
        if (e.type === "click") {
            this.onclick(e.target)
        }
    }
    onclick(e) {
        console.log("menu: onclick event")
        if (e && e.tagName === "A") {
            this.click(e)
            if (e.hasAttribute("value")) {
                router.navigate(parseInt(e.getAttribute("value")));
            }
        }
    }
}

const top = new Menu(document.getElementsByClassName("menu")[0]);
const navigation = {
    top
}

export default navigation;