import dom from '../utils/dom.js';
import {maskText} from '../utils/string-util.js';
import Component from "./component.js";

// constants
const maskCharacter = '_';
const className = "cards";
const tagName = "div"

class Cards extends Component {
    constructor(parent) {
        super(parent, null, tagName, className)
        this.local;
        this.cards = [];
        this.words;
        this.active = -1;
        this.status;
    }
    render(localData) {
        this.local = localData;
        this.reset();

        for (let tab = 0; tab < this.local.save.tabs.length; tab++) {
            this.createCard(this.element)
        }
        this.setActive(this.local.save.card);
    }
    setActive(index) {
        if (this.active > -1) {
            this.cards[this.active].classList.toggle('active', false);
        }
        this.local.save.card = index;
        this.active = index;
        this.cards[this.active].classList.toggle('active', true);
    }
    populate(row) {
        if (typeof row === "number") {
            this.local.save.row = row
        }
        this.words = this.local.data[this.local.save.row];
        for (let index = 0; index < this.cards.length; index++) {
            this.setContent(index, this.words ? this.words[index] : '')
        }
        this.update();
    }
    setContent(index, str) {
        this.cards[index].firstChild.textContent = str
    }
    setStatus(status, flag) {
        this.cards[this.active].classList.toggle(status, flag);
        this.cards[this.active].classList.toggle("active", !flag);
        this.status = flag ? status : null;
    }
    update(index) {
        if (this.active === -1 || !this.element) {
            return
        }
        if (typeof index === "number") {
            this.updateIndex(index)
        } else {
            for (let i = 0; i < this.cards.length; i++) {
                this.updateIndex(i)
            }
        }
    }
    updateIndex(i) {
        const isActiveTab = this.local.save.tabs[i];
        if (i === this.active) {
            this.visibleContent(i, true)
            if (isActiveTab || this.status === "error") {
                this.setContent(i, this.words[i])
            } else {
                this.setContent(i, maskText(this.words[i], maskCharacter))
            }
        } else {
            this.visibleContent(i, isActiveTab)
        }
    }
    createCard(parent) {
        const index = this.cards.length || 0;
        const card = dom.element('div', parent);
        card.id = 'card' + (index + 1);
        card.classList.add('card');
        dom.element('h4', card);
        this.cards.push(card)
    }
    isValid() {
        return this.active > -1  && this.cards.length > this.active
    }
    visibleContent(index, flag) {
        this.cards[index].firstChild.style.visibility = flag ? '' : 'hidden';
    }
    reset() {
        super.reset();
        this.cards.length = 0;
        this.local.save.row = 0;
        this.active = -1;
        this.status = null;
    }
}

export default Cards;